package tanvd.konfy.toml

import net.consensys.cava.toml.*
import tanvd.konfy.conversion.ConversionService
import tanvd.konfy.provider.ConfigProvider
import java.io.File
import java.lang.reflect.Type

/**
 * Config provider with TOML config file as backend
 * Does not use cache.
 */
class TomlProvider(private val client: TomlParseResult,
                   val convert: (String, Type) -> Any? = ConversionService::convert) : ConfigProvider() {
    constructor(configFile: File, convert: (String, Type) -> Any? = ConversionService::convert) : this(Toml.parse(configFile.readText()), convert)
    constructor(config: String, convert: (String, Type) -> Any? = ConversionService::convert) : this(Toml.parse(config), convert)

    @Suppress("UNCHECKED_CAST")
    override fun <N : Any> fetch(key: String, type: Type) = when (val value = client.get(key)) {
        null -> null
        is TomlArray -> when {
            type is Class<*> && type.isArray -> convert(value.toString(), type)
            else -> value.toList().map { convert(it.toString(), String::class.java) }
        }
        else -> convert(value.toString(), type)
    } as N?
}
