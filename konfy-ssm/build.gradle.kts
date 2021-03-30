import tanvd.kosogor.proxy.publishJar

group = rootProject.group
version = rootProject.version

dependencies {
    compile(project(":konfy"))

    compile("com.amazonaws", "aws-java-sdk-ssm", "1.11.555")
}

val bintrayUploadEnabled = System.getenv("bintray_key") != null
val artifactoryUploadEnabled = System.getenv("artifactory_url") != null

publishJar {
    if (bintrayUploadEnabled) {
        bintray {
            username = "tanvd"
            repository = "konfy"
            info {
                description = "Konfy SSM support"
                githubRepo = "https://github.com/TanVD/konfy"
                vcsUrl = "https://github.com/TanVD/konfy"
                labels.addAll(listOf("kotlin", "configuration", "kotlin-dsl", "konfy", "ssm"))
            }
        }
    }

    if (artifactoryUploadEnabled) {
        artifactory {
            secretKey = System.getenv("artifactory_api_key")
        }
    }
}


