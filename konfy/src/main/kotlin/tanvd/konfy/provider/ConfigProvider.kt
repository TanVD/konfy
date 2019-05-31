package tanvd.konfy.provider

import java.lang.reflect.Type
import kotlin.reflect.KClass

abstract class ConfigProvider {
    abstract fun fetch(key: String, type: Type): Any?

    inline fun <reified N> tryGet(key: String): N? = tryGet(key, N::class.java)
    @Suppress("UNCHECKED_CAST")
    fun <N> tryGet(key: String, type: Type): N? = fetch(key, type) as N?
    fun <N> tryGet(key: String, klass: KClass<*>): N? = tryGet(key, klass.java)

    inline fun <reified N> get(key: String, default: N? = null) = get(key, N::class.java, default)
    fun <N> get(key: String, type: Type, default: N? = null) = (tryGet<N>(key, type) ?: default)
    fun <N> get(key: String, klass: KClass<*>, default: N? = null) = get(key, klass.java, default)
}

