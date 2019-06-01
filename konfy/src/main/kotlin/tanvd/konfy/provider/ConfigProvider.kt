package tanvd.konfy.provider

import java.lang.reflect.Type
import kotlin.reflect.KClass

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
    fun <N : Any> get(key: String, type: Type, default: N? = null): N = (tryGet(key, type) ?: default) as N
    fun <N : Any> get(key: String, klass: KClass<*>, default: N? = null): N = get(key, klass.java, default)
}

