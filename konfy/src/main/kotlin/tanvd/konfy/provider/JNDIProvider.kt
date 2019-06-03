package tanvd.konfy.provider

import tanvd.konfy.conversion.ConversionService
import tanvd.konfy.utils.tryRun
import java.lang.reflect.Type
import javax.naming.Context
import javax.naming.InitialContext

/**
 * Config provider with JNDI variables as backend
 * Expects requested variables to be string
 * Does not use cache.
 */
class JNDIProvider(val convert: (String, Type) -> Any? = ConversionService::convert) : ConfigProvider() {
    @Suppress("UNCHECKED_CAST")
    override fun <N : Any> fetch(key: String, type: Type): N? {
        val environmentContext = InitialContext().lookup("java:comp/env") as Context
        val value = tryRun {
            environmentContext.lookup(key) as String
        } ?: return null
        return convert(value, type) as N?
    }
}
