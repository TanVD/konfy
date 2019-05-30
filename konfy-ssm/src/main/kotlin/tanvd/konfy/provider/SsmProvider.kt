package tanvd.konfy.provider

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest
import com.amazonaws.services.simplesystemsmanagement.model.ParameterNotFoundException
import tanvd.konfy.conversion.ConversionService
import java.lang.reflect.Type

/**
 * Provider takes values from AWS SSM.
 * Each key will be prepended with a prefix (with a dot after it) -- "${prefix}.${key}"
 * If prefix is null, then key itself will be used as key
 */
class SsmProvider(private val prefix: String?, private val ssm: AWSSimpleSystemsManagement = SsmClient.defaultClient,
                  val convert: (String, Type) -> Any? = ConversionService::convert) : ConfigProvider() {
    override fun fetch(key: String, type: Type): Any? {
        val fullKey = prefix?.let { "$it.$key" } ?: key
        val request = GetParameterRequest().withName(fullKey).withWithDecryption(true)
        return try {
            ssm.getParameter(request)?.parameter?.value?.trim()?.let { convert(it, type) }
        } catch (e: ParameterNotFoundException) {
            null
        }
    }
}
