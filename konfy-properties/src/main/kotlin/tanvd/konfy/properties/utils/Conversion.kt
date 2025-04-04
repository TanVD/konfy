package tanvd.konfy.properties.utils

import tanvd.konfy.conversion.ConversionService
import tanvd.konfy.utils.toTypedArray
import java.lang.reflect.Type

object Conversion {
    fun <N : Any> convert(value: String, type: Type): N? {
        when {
            (type is Class<*> && type.isArray) -> {
                val values = value.split(",").map { it.trim() }
                val converted = values.map { ConversionService.convert(it, type.componentType) }
                return converted.toTypedArray(type.componentType) as N?
            }

            else -> {
                return ConversionService.convert(value, type) as N?
            }
        }
    }
}