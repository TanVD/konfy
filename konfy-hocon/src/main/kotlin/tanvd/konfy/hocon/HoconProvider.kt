package tanvd.konfy.hocon

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import tanvd.konfy.provider.ConfigProvider
import java.io.File
import java.lang.reflect.Type

class HoconProvider(private val file: File) : ConfigProvider() {
    private val config: Config = ConfigFactory.parseFile(file)

    @Suppress("UNCHECKED_CAST")
    override fun <N : Any> fetch(key: String, type: Type): N? {
        return try {
            config.getAnyRef(key) as N?
        } catch (e: Exception) {
            null
        }
    }
}
