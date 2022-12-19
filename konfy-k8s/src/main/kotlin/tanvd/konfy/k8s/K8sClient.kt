package tanvd.konfy.k8s

import io.kubernetes.client.openapi.ApiClient
import io.kubernetes.client.util.Config

internal object K8sClient {
    val defaultClient: ApiClient by lazy { Config.defaultClient() }
}
