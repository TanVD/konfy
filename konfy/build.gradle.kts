import tanvd.kosogor.proxy.publishJar

group = rootProject.group
version = rootProject.version

dependencies {
    compile(kotlin("stdlib"))
    compile(kotlin("reflect"))
}

val bintrayUploadEnabled = System.getenv("bintray_key") != null
val artifactoryUploadEnabled = System.getenv("artifactory_url") != null

publishJar {
    publication {
        this.artifactId = "konfy"
    }
    if (bintrayUploadEnabled) {
        bintray {
            username = "tanvd"
            repository = "konfy"
            info {
                description = "Statically typed configurations for Kotlin"
                githubRepo = "https://github.com/TanVD/konfy"
                vcsUrl = "https://github.com/TanVD/konfy"
                labels.addAll(listOf("kotlin", "configuration", "kotlin-dsl", "konfy"))
            }
        }
    }

    if (artifactoryUploadEnabled) {
        artifactory {
            secretKey = System.getenv("artifactory_api_key")
        }
    }
}
