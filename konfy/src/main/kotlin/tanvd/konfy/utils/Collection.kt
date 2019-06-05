package tanvd.konfy.utils

/** Get first element, which evaluated to not null value. Otherwise, null. */
internal inline fun <T, E : Any> Iterable<T>.firstNotNull(func: (T) -> E?): E? {
    for (element in this) {
        val value = func(element)
        if (value != null) {
            return value
        }
    }
    return null
}
