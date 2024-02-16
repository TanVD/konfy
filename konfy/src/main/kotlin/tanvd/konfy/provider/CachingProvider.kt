package tanvd.konfy.provider

import com.google.common.util.concurrent.Striped
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
    private val locks = Striped.lazyWeakReadWriteLock(128)

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

//    @Suppress("UNCHECKED_CAST")
//    override fun <N : Any> fetch(key: String, type: Type): N? = cache.computeIfAbsent(key) {
//        val now = Clock.System.now()
//        val value = provider.tryGet<N>(it, type)
//        CacheEntry(now, value)
//    }.value as N?

    @Suppress("UNCHECKED_CAST")
    override fun <N : Any> fetch(key: String, type: Type): N? {
        val lock = locks.get(key)

        try {
            lock.readLock().lock()
            cache[key]?.let { return it.value as N? }
        } finally {
            lock.readLock().unlock()
        }

        try {
            lock.writeLock().lock()
            return cache.computeIfAbsent(key) {
                val now = Clock.System.now()
                val value = provider.tryGet<N>(key, type)
                CacheEntry(now, value)
            }.value as N?
        } finally {
            lock.writeLock().unlock()
        }
    }

}
