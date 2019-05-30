package tanvd.konfy.ssm

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder

internal object SsmClient {
    val defaultClient by lazy { AWSSimpleSystemsManagementClientBuilder.defaultClient()!! }
}
