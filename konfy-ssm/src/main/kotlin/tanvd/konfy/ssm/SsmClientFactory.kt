package tanvd.konfy.ssm

import software.amazon.awssdk.services.ssm.SsmClient


internal object SsmClientFactory {
    val defaultClient by lazy { SsmClient.create()!! }
}
