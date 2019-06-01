package tanvd.konfy.kara

import tanvd.konfy.conversion.ConversionService
import tanvd.konfy.provider.ConfigProvider
import java.io.File
import java.lang.reflect.Type

/**
 * Config provider with Kara config file and JNDI as backend
 * Uses cache.
 */
class KaraProvider(path: String, baseFile: File? = null,
                   val convert: (String, Type) -> Any? = ConversionService::convert) : ConfigProvider() {
    constructor(file: File, convert: (String, Type) -> Any? = ConversionService::convert) : this(file.name, file.parentFile, convert)

    private val config = Config().apply { Config.fillFrom(this, path, baseFile) }

    @Suppress("UNCHECKED_CAST")
    override fun <N : Any> fetch(key: String, type: Type): N? {
        return config.tryGet(key)?.let { convert(it, type) as N }
    }
}
