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
}

inline fun <R, reified N : Any, reified T> ConfigView.provided(key: String? = null, default: N? = null,
                                                               noinline transform: (N) -> T) = provider.provided<R, N, T>(key, default, transform)

inline fun <R, reified N> ConfigView.provided(key: String? = null, default: N? = null) = provider.provided<R, N>(key, default)
