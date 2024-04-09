package tanvd.konfy.ssm

import org.slf4j.LoggerFactory
import software.amazon.awssdk.services.ssm.SsmClient
import software.amazon.awssdk.services.ssm.model.GetParameterRequest
import software.amazon.awssdk.services.ssm.model.ParameterNotFoundException
import tanvd.konfy.conversion.ConversionService
import tanvd.konfy.provider.ConfigProvider
import java.lang.reflect.Type

private const val KONFY_LOG_KEYS = "KONFY_LOG_KEYS"

/**
 * Provider takes values from AWS SSM.
 * Each key will be prepended with a prefix (with a slash after it) -- "${prefix}/${key}"
 * If prefix is null, then key itself will be used as key
 * @param prefix prefix for SSM
 * @param separator separator used by a client, provider will map it to `/`
 */
class SsmProvider(private val prefix: String?, private val separator: String,
                  private val ssm: SsmClient = SsmClientFactory.defaultClient,
                  private val convert: (String, Type) -> Any? = ConversionService::convert) : ConfigProvider() {

    private val logger = LoggerFactory.getLogger(SsmProvider::class.java)
    private val konfyLogKeys = (System.getenv(KONFY_LOG_KEYS) ?: System.getProperty(KONFY_LOG_KEYS))
        ?.split(",")
        ?.map { it.trim() }.orEmpty()

    init {
        logger.info("SsmProvider initialized.")
        logger.info("Konfy log keys: $konfyLogKeys")
    }

    @Suppress("UNCHECKED_CAST")
    override fun <N : Any> fetch(key: String, type: Type): N? {
        log(key)

        val fullKey = (prefix?.let { "$it/$key" } ?: key).replace(separator, "/")
        val request = GetParameterRequest.builder().name(fullKey).withDecryption(true).build()
        return try {
            ssm.getParameter(request)?.parameter()?.value()?.let { convert(it, type) as N }
        } catch (e: ParameterNotFoundException) {
            null
        }
    }

    private fun log(key: String) {
        if (konfyLogKeys.contains(key)) {
            val stackTrace = Thread.currentThread().stackTrace.joinToString("\n")
            logger.info("Fetching parameter with key: $key \n Stack trace: \n $stackTrace")
        }
    }

}
