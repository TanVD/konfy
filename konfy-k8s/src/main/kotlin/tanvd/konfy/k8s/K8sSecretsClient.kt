package tanvd.konfy.k8s

import io.kubernetes.client.util.Config

internal object K8sSecretsClient {
    val defaultClient by lazy { Config.defaultClient() }
}
