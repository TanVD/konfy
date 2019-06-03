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
        private var isCached = false

        @Volatile
        private var cache: N? = null

        @Suppress("UNCHECKED_CAST")
        override operator fun getValue(thisRef: R, property: KProperty<*>): N {
            val getKey = key ?: property.name

            if (!cached) {
                return (provider.tryGet<Any>(getKey, property.returnType.jvmErasure) ?: default) as N
            }

            if (isCached) {
                return cache as N
            }

            cache = (provider.tryGet<Any>(getKey, property.returnType.jvmErasure) ?: default) as N?
            isCached = true

            if (property.returnType.isMarkedNullable && cache == null) {
                throw IllegalStateException("Not found key $getKey in a provider, but property was not nullable.")
            }

            return cache as N
        }

        fun reset() {
            isCached = false
            cache = null
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
