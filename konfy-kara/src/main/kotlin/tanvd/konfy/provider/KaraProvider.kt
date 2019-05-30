package tanvd.konfy.provider

import tanvd.konfy.conversion.ConversionService
import java.io.File
import java.lang.reflect.Type

class KaraProvider(path: String, baseFile: File? = null,
                   val convert: (String, Type) -> Any? = ConversionService::convert) : ConfigProvider() {
    private val config = Config().apply { Config.fillFrom(this, path, baseFile) }

    override fun fetch(key: String, type: Type): Any? {
        return config.tryGet(key)?.let { convert(it, type) }
    }
}
