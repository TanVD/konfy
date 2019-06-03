package tanvd.konfy.utils

internal fun <T> tryRun(body: () -> T): T? = try {
    body()
} catch (e: Throwable) {
    null
}
