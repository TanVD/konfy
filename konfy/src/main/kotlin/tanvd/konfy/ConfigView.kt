package tanvd.konfy

import tanvd.konfy.provider.ConfigProvider

/**
 * [ConfigView] is a read-only view to [ConfigProvider].
 * It uses [GlobalKonfy] provider by default.
 *
 * [ConfigView] provides handful delegate [provided] to
 * get the parameters from chain in a typeful way.
 *
 * [provided] delegate will cache values by default.
 */
interface ConfigView {
    val provider: ConfigProvider
        get() = GlobalKonfy.provider

    fun <R, N> provided(key: String? = null, default: N? = null) = provider.provided<R, N>(key, default)

    fun <R, N: Any, T> provided(key: String? = null, default: N? = null, transform: (N) -> T) = provider.provided<R, N, T>(key, default, transform)
}
