package tanvd.konfy.provider

import java.lang.reflect.Type
import javax.naming.*

/**
 * Config provider with JNDI variables as backend
 *
 * Requested values will not be converted, JNDI should do it
 *
 * Does not use cache.
 */
class JNDIProvider(val name: String = "java:comp/env") : ConfigProvider() {
    private val context by lazy { InitialContext().lookup(name) as Context }

    @Suppress("UNCHECKED_CAST")
    override fun <N : Any> fetch(key: String, type: Type): N? = try {
        context.lookup(key) as N
    } catch (e: NamingException) {
        null
    }
}
