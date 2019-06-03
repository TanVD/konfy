package tanvd.konfy

import tanvd.konfy.provider.ConfigProvider
import tanvd.konfy.utils.firstNotNull
import java.lang.reflect.Type

/**
 * [ConfigChain] is a provider that connects other ConfigProviders into a chain.
 *
 * On a [fetch] key will be searched in providers one by one until one of them will
 * return not null. Then [ConfigChain] will return this value, otherwise it will return null.
 */
class ConfigChain(private val providers: List<ConfigProvider>) : ConfigProvider() {
    constructor(vararg providers: ConfigProvider) : this(providers.toList())

    override fun <N : Any> fetch(key: String, type: Type): N? {
        return providers.firstNotNull { it.tryGet<N>(key, type) }
    }
}

