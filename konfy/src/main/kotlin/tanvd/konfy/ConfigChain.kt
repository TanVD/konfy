package tanvd.konfy

import tanvd.konfy.provider.ConfigProvider
import tanvd.konfy.utils.firstNotNull
import java.lang.reflect.Type
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.jvmErasure

open class ConfigChain(private val providers: List<ConfigProvider>) {
    constructor(vararg providers: ConfigProvider) : this(providers.toList())

    private val properties = HashSet<PropertyProvider<*, *>>()

    inner class PropertyProvider<R, N>(private val key: String?, private val default: N?) : ReadOnlyProperty<R, N> {
        @Volatile
        private var isCached = false

        @Volatile
        private var cache: N? = null

        @Suppress("UNCHECKED_CAST")
        override operator fun getValue(thisRef: R, property: KProperty<*>): N {
            val getKey = key ?: property.name
            if (isCached) {
                return cache as N
            }

            cache = get(getKey, property.returnType.jvmErasure, default)
            isCached = true

            if (property.returnType.isMarkedNullable && cache == null) {
                throw IllegalStateException("Not found key $getKey in provider chain, but property was not nullable.")
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
     */
    fun <R, N> provided(key: String? = null, default: N? = null) = PropertyProvider<R, N>(key, default)

    inline fun <reified N> get(key: String, default: N? = null): N? = get(key, N::class, default)
    fun <N> get(key: String, type: Type, default: N? = null) = providers.firstNotNull { it.get(key, type, default) }
    fun <N> get(key: String, klass: KClass<*>, default: N? = null) = get(key, klass.java, default)

    /** Reset caches for all properties. */
    fun reset() {
        properties.forEach {
            it.reset()
        }
    }
}

