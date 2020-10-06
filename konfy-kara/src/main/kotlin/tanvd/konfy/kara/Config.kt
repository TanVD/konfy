package tanvd.konfy.kara

import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.regex.Pattern
import javax.naming.*

internal class Config {
    private val cache = ConcurrentHashMap<String, Optional<String>>()

    fun tryGet(name: String): String? = cache.getOrPut(name) {
        Optional.ofNullable(lookupJNDI(name))
    }.orElse(null)


    /** Sets a value for the given key. */
    private operator fun set(name: String, value: String) {
        cache[name] = Optional.of(lookupJNDI(name) ?: value)
    }

    private fun lookupJNDI(name: String): String? = try {
        val envCtx = InitialContext().lookup("java:comp/env") as Context
        envCtx.lookup(name) as String
    } catch (e: NamingException) {
        null
    }

    companion object {
        private val varPattern = Pattern.compile("\\$\\{([^}]*)}")

        internal fun fillFrom(config: Config, path: String, classloader: ClassLoader, baseFile: File? = null) {
            fun eval(name: String): String =
                config.tryGet(name) ?: System.getProperty(name) ?: System.getenv(name) ?: error("$name is not defined")

            val resolvedPath = renderVars(path, ::eval)

            val file = if (resolvedPath.startsWith('/') || baseFile == null) File(resolvedPath) else File(baseFile, resolvedPath)

            val (baseFile: File?, text: String) = if (file.exists()) {
                file.parentFile to file.readText(Charsets.UTF_8)
            } else {
                val resourceFile = file.path.replace(File.separator, "/")
                val resource = classloader.getResourceAsStream(resourceFile)
                if (resource != null) {
                    null to resource.reader(Charsets.UTF_8).readText()
                } else {
                    error("Could not found a config file $resourceFile")
                }
            }

            text.reader().forEachLine {
                val line = it.trim()
                when {
                    line.startsWith("include ") -> fillFrom(config, line.removePrefix("include "), classloader, baseFile)
                    line.startsWith("log ") -> {
                    }
                    line.startsWith("#") || line.isEmpty() -> {
                    }

                    else -> {
                        val eq = line.indexOf('=')
                        if (eq <= 0) error("Cannot parse line '$line' in file '${file.absolutePath}'")
                        config[line.substring(0, eq).trim()] = renderVars(line.substring(eq + 1).trim(), ::eval)
                    }
                }
            }
        }

        private fun renderVars(line: String, eval: (String) -> String) = buildString {
            val matcher = varPattern.matcher(line)
            var lastAppend = 0

            while (matcher.find()) {
                val varName = matcher.group(1)!!
                append(line, lastAppend, matcher.start())
                append(eval(varName))
                lastAppend = matcher.end()
            }

            append(line, lastAppend, line.length)
        }
    }
}
