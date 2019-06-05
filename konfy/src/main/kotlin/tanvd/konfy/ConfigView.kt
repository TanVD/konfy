package tanvd.konfy

import tanvd.konfy.provider.ConfigProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.jvmErasure

/**
 * [ConfigView] is a read-only view to [ConfigProvider]
 *
 * It provides handful delegate [provided] to get the
 * parameters from chain in a strongly typed way.
 *
 * Also, this delegate is capable of caching.
 *
 * Note, that caching is enabled by default.
 */
open class ConfigView(val provider: ConfigProvider = GlobalKonfy.provider) {
    inner class PropertyProvider<R, N>(private val key: String?, private val default: N?,
                                       private val cache: Boolean, private val transform: (N) -> N) : ReadOnlyProperty<R, N> {
        @Volatile
        private var wasCached = false

        private var cached: N? = null

        @Suppress("UNCHECKED_CAST")
        override operator fun getValue(thisRef: R, property: KProperty<*>): N {
            if (wasCached) {
                return cached as N
            }

            if (!cache) {
                return get(property) as N
            }

            synchronized(this) {
                if (wasCached) {
                    return cached as N
                }

                cached = get(property)
                wasCached = true
            }

            return cached as N
        }

        @Suppress("UNCHECKED_CAST")
        private fun get(property: KProperty<*>): N? = (
                provider.tryGet<Any>(key ?: property.name, property.returnType.jvmErasure) ?: default
                )?.let { transform(it as N) }

    }

    /**
     * Provides value from one of ConfigProviders (getting first not null value)
     * By default name of property will be used as key.
     * By default, no default value is provided
     *
     * @param key override key to pass to ConfigProviders
     * @param default default value to return if it cannot be found
     * @param cache should this property be cached
     */
    fun <R, N> provided(key: String? = null, default: N? = null,
                        cache: Boolean = true, transform: (N) -> N = { it }) = PropertyProvider<R, N>(key, default, cache, transform)
}
