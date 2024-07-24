package tanvd.konfy.properties

import tanvd.konfy.conversion.ConversionService
import tanvd.konfy.provider.ConfigProvider
import tanvd.konfy.utils.toTypedArray
import java.lang.reflect.Type
import java.util.Properties

class PropertiesProvider(private val properties: Properties) : ConfigProvider() {
    override fun <N : Any> fetch(key: String, type: Type): N? {
        val value = properties[key]?.toString() ?: return null
        when {
            (type is Class<*> && type.isArray) -> {
                val values = value.split(",").map { it.trim() }
                val converted = values.map { ConversionService.convert(it, type.componentType) }
                return converted.toTypedArray(type.componentType) as N?
            }

            else -> {
                val converted = ConversionService.convert(value, type)
                return converted as N?
            }
        }
    }

    companion object {
        fun from(value: String): PropertiesProvider {
            val properties = Properties()
            properties.load(value.byteInputStream())
            return PropertiesProvider(properties)
        }
    }
}