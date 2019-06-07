package tanvd.konfy.provider

import tanvd.konfy.conversion.ConversionService
import java.io.File
import java.lang.reflect.Type
import java.util.*

/**
 * Config provider with Properties file as backend
 * Does not use cache.
 */
class PropertiesProvider(private val file: File,
                         private val convert: (String, Type) -> Any? = ConversionService::convert) : ConfigProvider() {
    private val properties by lazy { Properties().apply { load(file.reader()) } }

    override fun <N : Any> fetch(key: String, type: Type): N? {
        val value = properties.getProperty(key) ?: return null
        return convert(value, type) as N?
    }
}
