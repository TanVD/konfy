package tanvd.konfy.provider

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration

/** ConfigProvider that caches all config values. */
class CachingProvider(private val provider: ConfigProvider, private val invalidationPeriod: Duration = Duration.INFINITE) : ConfigProvider() {
    class CacheEntry(val mark: Instant, val value: Any)

    private val cache = ConcurrentHashMap<String, CacheEntry>()

    @Suppress("UNCHECKED_CAST")
    override fun <N : Any> fetch(key: String, type: Type): N? {
        val value = cache[key]?.value ?: synchronized(key.intern()) {
            val entry = cache[key]
            if (entry == null || Clock.System.now().minus(entry.mark) > invalidationPeriod) {
                val value = provider.tryGet<N>(key, type)
                if (value != null) {
                    cache[key] = CacheEntry(Clock.System.now(), value)
                }
                value
            } else {
                entry.value
            }
        }
        return value as N?
    }
}
