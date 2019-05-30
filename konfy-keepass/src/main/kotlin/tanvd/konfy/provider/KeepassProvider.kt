import tanvd.konfy.conversion.ConversionService
import tanvd.konfy.provider.ConfigProvider
import tanvd.konfy.provider.KeepassClient
import java.io.File
import java.lang.reflect.Type

class KeepassProvider(database: File, password: String,
                      val convert: (String, Type) -> Any? = ConversionService::convert) : ConfigProvider() {
    private val client = KeepassClient(database, password)

    override fun fetch(key: String, type: Type): Any? {
        return client.get(key)?.let { convert(it, type) }
    }
}
