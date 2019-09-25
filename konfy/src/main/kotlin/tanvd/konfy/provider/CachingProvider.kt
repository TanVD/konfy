package tanvd.konfy.provider

import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap

/**
 * ConfigProvider that caches all config values.
 *
 * It will
 */
class CachingProvider(private val provider: ConfigProvider) : ConfigProvider() {
    private object Null

    private val cache = ConcurrentHashMap<String, Any>()

    @Suppress("UNCHECKED_CAST")
    override fun <N : Any> fetch(key: String, type: Type): N? {
        val res = cache.getOrPut(key) {
            provider.tryGet(key, type) ?: Null
        }
        if (res == Null) return null
        return res as N
    }
}
