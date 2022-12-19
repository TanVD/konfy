package tanvd.konfy.k8s

import io.kubernetes.client.openapi.ApiClient
import io.kubernetes.client.openapi.ApiException
import io.kubernetes.client.openapi.apis.CoreV1Api
import tanvd.konfy.conversion.ConversionService
import tanvd.konfy.provider.ConfigProvider
import java.lang.reflect.Type

/**
 * Provider takes values from K8S ConfigMaps.
 * Each key will be extracted from [configMapName] secret
 * @param namespace K8S namespace
 * @param configMapName ConfigMap name
 */
class K8sConfigMapProvider(
    private val namespace: String = "default",
    private val configMapName: String,
    private val k8sClient: ApiClient = K8sClient.defaultClient,
    private val convert: (String, Type) -> Any? = ConversionService::convert
) : ConfigProvider() {
    private val api by lazy { CoreV1Api(k8sClient) }

    @Suppress("UNCHECKED_CAST")
    override fun <N : Any> fetch(key: String, type: Type): N? {
        val configMapValue = runCatching {
            api.readNamespacedConfigMap(configMapName, namespace, null)
        }.onFailure {
            if (it is ApiException) {
                throw ApiException("${it.message}: ${it.responseBody}")
            }
        }.getOrThrow()
        return configMapValue?.data?.get(key)?.let { convert(it, type) as N }
    }
}
