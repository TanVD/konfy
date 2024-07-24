package tanvd.konfy.properties

import tanvd.konfy.properties.utils.Conversion
import tanvd.konfy.provider.ConfigProvider
import java.lang.reflect.Type
import java.util.*

class PropertiesProvider(private val properties: Properties) : ConfigProvider() {
    override fun <N : Any> fetch(key: String, type: Type): N? {
        val value = properties[key]?.toString() ?: return null
        return Conversion.convert(value, type) as N?
    }

    companion object {
        /**
         * Read properties from [value] string and return corresponding [PropertiesProvider]
         */
        fun from(value: String): PropertiesProvider {
            val properties = Properties()
            properties.load(value.byteInputStream())
            return PropertiesProvider(properties)
        }
    }
}