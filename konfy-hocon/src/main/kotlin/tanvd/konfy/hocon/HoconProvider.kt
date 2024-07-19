package tanvd.konfy.hocon

import com.typesafe.config.Config
import tanvd.konfy.provider.ConfigProvider
import tanvd.konfy.conversion.ConversionService
import java.lang.reflect.Type

class HoconProvider(private val config: Config, private val convert: (String, Type) -> Any? = ConversionService::convert) : ConfigProvider() {

    @Suppress("UNCHECKED_CAST")
    override fun <N : Any> fetch(key: String, type: Type): N? {
        val value = config.getString(key) ?: return null
        return convert(value, type) as N?
    }
}
