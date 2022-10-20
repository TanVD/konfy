package tanvd.konfy.kara

import tanvd.konfy.conversion.ConversionService
import tanvd.konfy.kara.utils.toTypedArray
import tanvd.konfy.provider.ConfigProvider
import java.io.File
import java.lang.reflect.Type

/**
 * Config provider with Kara config file and JNDI as backend
 * Uses cache.
 */
class KaraProvider(path: String, baseFile: File? = null, classLoader: ClassLoader,
                   private val convert: (String, Type) -> Any? = ConversionService::convert) : ConfigProvider() {
    constructor(file: File, classLoader: ClassLoader, convert: (String, Type) -> Any? = ConversionService::convert) : this(file.name, file.parentFile, classLoader, convert)

    private val config = Config().apply { Config.fillFrom(this, path, classLoader, baseFile) }

    @Suppress("UNCHECKED_CAST")
    override fun <N : Any> fetch(key: String, type: Type): N? {
        return config.tryGet(key)?.let { value ->
            if (type is Class<*> && type.isArray) {
                value.split(",")
                    .map { it.trim() }
                    .map { convert(it, type.componentType) }
                    .toTypedArray(type.componentType)
            } else {
                convert(value, type)
            } as N
        }
    }
}
