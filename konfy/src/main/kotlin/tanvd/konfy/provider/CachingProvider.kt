package tanvd.konfy.provider

import kotlinx.coroutines.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

/** ConfigProvider that caches all config values. */
class CachingProvider(
    private val provider: ConfigProvider,
    private val lifetime: Duration = Duration.INFINITE,
    private val period: Duration = lifetime,
    private val context: CoroutineContext = Dispatchers.Default
) : ConfigProvider() {
    class CacheEntry(val mark: Instant, val value: Any?)

    private val cache = ConcurrentHashMap<String, CacheEntry>()

    init {
        if (lifetime != Duration.INFINITE) {
            runInvalidator()
        }
    }

    private fun runInvalidator() {
        CoroutineScope(context + CoroutineExceptionHandler { _, _ ->
            runInvalidator()
        }).launch {
            while (currentCoroutineContext().isActive) {
                val now = Clock.System.now()
                cache.entries.removeIf { (_, entry) -> entry.mark.plus(lifetime) < now }
                delay(period)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <N : Any> fetch(key: String, type: Type): N? {
        val now = Clock.System.now()
        return cache.computeIfAbsent(key) {
            val value = provider.tryGet<N>(it, type)
            CacheEntry(now, value)
        }.value as N?
    }
}
