package tanvd.konfy.provider

import java.lang.reflect.Type
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * [ConfigProvider] interface.
 *
 * Each config provider should implement [fetch] function - this
 * function retrieve values from config backend (like config file)
 * and converts them to needed type.
 *
 * For conversion service [ConfigProvider] may use [tanvd.konfy.conversion.ConversionService]
 *
 * [ConfigProvider] implementation may use caching - depending on a
 * performance reasons. If [ConfigProvider] uses cache it should
 * be stated in its docs.
 */
abstract class ConfigProvider {
    abstract fun <N : Any> fetch(key: String, type: Type): N?

    inline fun <reified N : Any> tryGet(key: String): N? = tryGet(key, N::class.java)
    fun <N : Any> tryGet(key: String, type: Type): N? = fetch(key, type)
    fun <N : Any> tryGet(key: String, klass: KClass<*>): N? = tryGet(key, klass.java)

    inline fun <reified N : Any> get(key: String, default: N? = null): N = get(key, N::class.java, default)
    fun <N : Any> get(key: String, type: Type, default: N? = null): N = checkNotNull(tryGet(key, type) ?: default) {
        "Non-nullable property '$key' must contain a non-null value"
    }
    fun <N : Any> get(key: String, klass: KClass<*>, default: N? = null): N = get(key, klass.java, default)

    inner class PropertyProvider<R, N, T>(private val key: String?, private val default: N?, private val transform: (N) -> T,
                                          private val nKlass: KClass<*>) : ReadOnlyProperty<R, T> {
        override operator fun getValue(thisRef: R, property: KProperty<*>) = get(property)

        @Suppress("UNCHECKED_CAST")
        private fun get(property: KProperty<*>): T {
            val getKey = key ?: property.name
            val result = (tryGet<Any>(getKey, nKlass) ?: default) as N?
            val transformed = result?.let { transform(it) }
            check(property.returnType.isMarkedNullable || transformed != null) { "Not found key $getKey in a provider, but property was not nullable." }
            return transformed as T
        }
    }

    /**
     * Provides value from a provider (getting first not null value)
     *
     * @param key override key to pass to ConfigProviders (name of variable if not set)
     * @param default default value to return if it cannot be found
     */
    inline fun <R, reified N> provided(key: String? = null, default: N? = null) = PropertyProvider<R, N, N>(key, default, { it }, N::class)

    /**
     * Provides value from a provider (getting first not null value)
     *
     * @param key override key to pass to ConfigProviders (name of variable if not set)
     * @param transform transforming function of property, that will be invoked only if property found
     */
    inline fun <R, reified N : Any, reified T> provided(key: String? = null, default: N? = null,
                                                        noinline transform: (N) -> T) = PropertyProvider<R, N, T>(key, default, transform, N::class)
}

