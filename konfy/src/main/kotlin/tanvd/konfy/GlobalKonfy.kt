package tanvd.konfy

import tanvd.konfy.provider.ConfigProvider
import kotlin.properties.Delegates

/**
 * Global configuration interface. It encapsulates all the configuration
 * available to the application.
 *
 * [ConfigView] objects will use it by default, if no other provider
 * set for them
 */
object GlobalKonfy {
    var isInitialized: Boolean = false
        private set(value) {
            field = value
        }

    @Suppress("ObjectPropertyName")
    private var _provider: ConfigProvider by Delegates.notNull()
    val provider: ConfigProvider
        get() = _provider

    @Synchronized
    fun init(provider: ConfigProvider) {
        if (isInitialized) error("GlobalKonfy is already initialized")
        this._provider = provider
    }

    inline fun <reified T : Any> tryGet(value: String): T? = provider.tryGet(value)
    inline fun <reified T : Any> get(value: String, default: T? = null): T = provider.get(value, default)
    inline operator fun <reified T : Any> invoke(value: String, default: T? = null): T = get(value, default)

    inline fun <reified T : Any> tryLazy(value: String) = lazy { tryGet<T>(value) }
    inline fun <reified T : Any> lazy(value: String, default: T? = null) = lazy { invoke(value, default) }
}
