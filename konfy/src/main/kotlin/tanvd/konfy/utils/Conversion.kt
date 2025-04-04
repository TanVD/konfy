package tanvd.konfy.utils

fun Collection<*>.toTypedArray(type: Class<*>): Array<*> {
    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    return (this as java.util.Collection<*>).toArray(java.lang.reflect.Array.newInstance(type, size) as Array<*>)
}
