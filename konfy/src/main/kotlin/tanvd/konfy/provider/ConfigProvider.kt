package tanvd.konfy.provider

import java.lang.reflect.Type
import kotlin.reflect.KClass

abstract class ConfigProvider {
    abstract fun fetch(key: String, type: Type): Any?

    inline fun <reified N> get(key: String, default: N? = null) = get(key, N::class.java, default)

    fun <N> get(key: String, type: Type, default: N? = null) = (fetch(key, type) ?: default) as N?
    fun <N> get(key: String, klass: KClass<*>, default: N? = null) = get(key, klass.java, default)
}

