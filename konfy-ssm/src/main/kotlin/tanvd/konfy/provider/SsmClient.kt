package tanvd.konfy.provider

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder

internal object SsmClient {
    val defaultClient by lazy { AWSSimpleSystemsManagementClientBuilder.defaultClient()!! }
}
