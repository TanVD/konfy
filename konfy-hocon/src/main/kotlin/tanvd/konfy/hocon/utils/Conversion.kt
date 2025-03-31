package tanvd.konfy.hocon.utils

import com.typesafe.config.ConfigValue
import tanvd.konfy.conversion.ConversionService
import java.lang.reflect.Type

object Conversion {
    fun convert(value: ConfigValue, type: Type): Any? {
        return when (value.valueType()) {
            ConfigValueType.OBJECT -> value.unwrapped()
            ConfigValueType.LIST -> (value.unwrapped() as List<*>).map { ConversionService.convert(it.toString(), type) }
            else -> ConversionService.convert(value.unwrapped().toString(), type)
        }
    }
}
