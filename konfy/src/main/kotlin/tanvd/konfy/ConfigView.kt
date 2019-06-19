package tanvd.konfy

import tanvd.konfy.provider.ConfigProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.jvmErasure

/**
 * [ConfigView] is a read-only view to [ConfigProvider].
 * It uses [GlobalKonfy] provider by default.
 *
 * [ConfigView] provides handful delegate [provided] to
 * get the parameters from chain in a typeful way.
 *
 * [provided] delegate will cache values by default.
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
                return get(property)
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
        private fun get(property: KProperty<*>): N {
            val getKey = key ?: property.name
            val result = provider.tryGet<Any>(getKey, property.returnType.jvmErasure) ?: default
            val transformed = result?.let { transform(it as N) }
            if (!property.returnType.isMarkedNullable && transformed == null) {
                throw IllegalStateException("Not found key $getKey in a provider, but property was not nullable.")
            }
            return transformed as N
        }
    }

    /**
     * Provides value from a provider (getting first not null value)
     *
     * @param key override key to pass to ConfigProviders (name of variable if not set)
     * @param default default value to return if it cannot be found
     * @param cache should this property be cached (true if not set)
     * @param transform transforming function of property
     */
    fun <R, N> provided(key: String? = null, default: N? = null,
                        cache: Boolean = true, transform: (N) -> N = { it }) = PropertyProvider<R, N>(key, default, cache, transform)
}
