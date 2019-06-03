package tanvd.konfy

import tanvd.konfy.provider.ConfigProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.jvmErasure

/**
 * [ConfigView] is a read-only view to [ConfigChain]
 *
 * It provides handful delegate [provided] to get the
 * parameters from chain in a strongly typed way.
 *
 * Also, this delegate is capable of caching.
 *
 * Note, that caching is enabled by default.
 */
open class ConfigView(val provider: ConfigProvider) {
    private val properties = HashSet<PropertyProvider<*, *>>()

    inner class PropertyProvider<R, N>(private val key: String?, private val default: N?, private val cached: Boolean) : ReadOnlyProperty<R, N> {
        @Volatile
        private var wasCached = false

        @Volatile
        private var cache: N? = null

        @Suppress("UNCHECKED_CAST")
        override operator fun getValue(thisRef: R, property: KProperty<*>): N {
            if (wasCached) {
                return cache as N
            }

            if (!cached) {
                return get(property) as N
            }

            cache = get(property)
            wasCached = true

            return cache as N
        }

        @Suppress("UNCHECKED_CAST")
        private fun get(property: KProperty<*>): N? = (provider.tryGet<Any>(key ?: property.name, property.returnType.jvmErasure) ?: default) as N?

        fun reset() {
            cache = null
            wasCached = false
        }
    }

    /**
     * Provides value from one of ConfigProviders (getting first not null value)
     * By default name of property will be used as key.
     * By default, no default value is provided
     *
     * @param key override key to pass to ConfigProviders
     * @param default default value to return if it cannot be found
     * @param cached should this property be cached
     */
    fun <R, N> provided(key: String? = null, default: N? = null, cached: Boolean = true) = PropertyProvider<R, N>(key, default, cached)

    /** Reset caches for all properties. */
    fun reset() {
        properties.forEach {
            it.reset()
        }
    }
}
