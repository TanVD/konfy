package tanvd.konfy.keepass

import tanvd.konfy.conversion.ConversionService
import tanvd.konfy.provider.ConfigProvider
import java.io.File
import java.lang.reflect.Type

/**
 * Config provider with Keepass db file (kdbx) as backend
 * Does not use cache.
 */
class KeepassProvider(database: File, password: String,
                      val convert: (String, Type) -> Any? = ConversionService::convert) : ConfigProvider() {
    private val client = KeepassClient(database, password)

    @Suppress("UNCHECKED_CAST")
    override fun <N : Any> fetch(key: String, type: Type): N? {
        return client.get(key)?.let { convert(it, type) as N }
    }
}
