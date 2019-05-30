package tanvd.konfy.conversion

import java.lang.reflect.Type
import java.lang.reflect.WildcardType
import java.math.BigDecimal
import java.math.BigInteger
import java.net.URLDecoder
import kotlin.reflect.KClass

object ConversionService {
    fun convert(value: String, type: Type): Any = when (type) {
        is WildcardType -> convert(value, type.upperBounds.single())
        Int::class.java, java.lang.Integer::class.java -> value.toInt()
        Float::class.java, java.lang.Float::class.java -> value.toFloat()
        Double::class.java, java.lang.Double::class.java -> value.toDouble()
        Long::class.java, java.lang.Long::class.java -> value.toLong()
        Boolean::class.java, java.lang.Boolean::class.java -> value.toBoolean()
        String::class.java, java.lang.String::class.java -> URLDecoder.decode(value, "UTF-8")
        BigDecimal::class.java -> BigDecimal(value)
        BigInteger::class.java -> BigInteger(value)
        else -> {
            if (type is Class<*> && type.isEnum) {
                type.enumConstants?.firstOrNull { (it as Enum<*>).name == value }
                        ?: throw ConversionException("Value $value is not an enum member name of $type")
            } else {
                throw ConversionException("Type $type is not supported in default data conversion service")
            }
        }
    }

    inline fun <reified T : Any> convert(value: String): T {
        return convert(value, T::class.java) as T
    }
}


