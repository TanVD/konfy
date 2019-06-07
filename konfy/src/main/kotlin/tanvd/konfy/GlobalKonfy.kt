package tanvd.konfy

import tanvd.konfy.provider.ConfigProvider
import java.lang.reflect.Type
import kotlin.properties.Delegates

/**
 * Global configuration interface.
 *
 * It encapsulates all the configuration of the application.
 *
 * Should be initialized with [init] call at the start of the
 * application.
 *
 * [ConfigView] objects will use it as a [ConfigProvider] by default,
 * if no other provider is used for them
 */
object GlobalKonfy: ConfigProvider() {
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

    override fun <N : Any> fetch(key: String, type: Type): N? = provider.fetch(key, type)

    inline operator fun <reified T : Any> invoke(value: String, default: T? = null): T = get(value, default)

    inline fun <reified T : Any> tryLazy(value: String) = lazy { tryGet<T>(value) }
    inline fun <reified T : Any> lazy(value: String, default: T? = null) = lazy { invoke(value, default) }
}
