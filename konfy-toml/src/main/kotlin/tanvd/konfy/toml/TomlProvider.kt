package tanvd.konfy.toml

import net.consensys.cava.toml.*
import tanvd.konfy.conversion.ConversionService
import tanvd.konfy.provider.ConfigProvider
import tanvd.konfy.toml.utils.toTypedArray
import java.io.File
import java.lang.reflect.Type

/**
 * Config provider with TOML config file as backend
 * Does not use cache.
 */
class TomlProvider(private val client: TomlParseResult,
                   private val convert: (String, Type) -> Any? = ConversionService::convert) : ConfigProvider() {
    constructor(configFile: File, convert: (String, Type) -> Any? = ConversionService::convert) : this(Toml.parse(configFile.readText()), convert)
    constructor(config: String, convert: (String, Type) -> Any? = ConversionService::convert) : this(Toml.parse(config), convert)

    @Suppress("UNCHECKED_CAST")
    override fun <N : Any> fetch(key: String, type: Type) = tomlConvert(client.get(key), type) as N?

    private fun tomlConvert(value: Any?, type: Type): Any? = when (value) {
        null -> null
        is TomlArray -> when {
            type is Class<*> && type.isArray -> value.toList().map { tomlConvert(it, type.componentType) }.toTypedArray(type.componentType)
            else -> value.toList().map { convert(it.toString(), String::class.java) }
        }
        else -> convert(value.toString(), type)
    }
}
