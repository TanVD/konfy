package tanvd.konfy.provider

import tanvd.konfy.conversion.ConversionService
import java.lang.reflect.Type

class EnvVarProvider(val convert: (String, Type) -> Any? = ConversionService::convert) : ConfigProvider() {
    override fun fetch(key: String, type: Type): Any? {
        val value = System.getenv(key) ?: return null
        return convert(value, type)
    }
}
