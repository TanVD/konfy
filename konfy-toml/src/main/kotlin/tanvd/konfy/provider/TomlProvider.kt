import net.consensys.cava.toml.*
import tanvd.konfy.conversion.ConversionService
import tanvd.konfy.provider.ConfigProvider
import java.io.File
import java.lang.reflect.Type

class TomlProvider(private val client: TomlParseResult,
                   val convert: (String, Type) -> Any? = ConversionService::convert) : ConfigProvider() {
    constructor(configFile: File, convert: (String, Type) -> Any? = ConversionService::convert) : this(Toml.parse(configFile.readText()), convert)
    constructor(config: String, convert: (String, Type) -> Any? = ConversionService::convert) : this(Toml.parse(config), convert)

    override fun fetch(key: String, type: Type): Any? {
        return when (val value = client.get(key)) {
            null -> null
            is TomlArray -> when {
                value.containsBooleans() -> value.toList().map { convert(it.toString(), Boolean::class.java) }
                value.containsDoubles() -> value.toList().map { convert(it.toString(), Double::class.java) }
                value.containsLongs() -> value.toList().map { convert(it.toString(), Long::class.java) }
                value.containsStrings() -> value.toList().map { convert(it.toString(), String::class.java) }
                else -> value.toList().map { convert(it.toString(), String::class.java) }
            }
            else -> convert(value.toString(), type)
        }
    }
}
