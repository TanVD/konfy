package tanvd.konfy.provider

import tanvd.konfy.conversion.ConversionService
import java.lang.reflect.Type

/**
 * Config provider with System properties as backend
 * Does not use cache.
 */
class SysPropProvider(val convert: (String, Type) -> Any? = ConversionService::convert) : ConfigProvider() {
    @Suppress("UNCHECKED_CAST")
    override fun <N : Any> fetch(key: String, type: Type): N? {
        val value = System.getProperty(key) ?: return null
        return convert(value, type) as N?
    }
}
