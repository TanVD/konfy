import tanvd.kosogor.proxy.publishJar

group = rootProject.group
version = rootProject.version

dependencies {
    compile(project(":konfy"))

    compile("org.tomlj", "tomlj", "1.0.0")

    testCompile("org.assertj", "assertj-core", "3.11.1")
    testCompile("org.junit.jupiter", "junit-jupiter-api", "5.2.0")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.2.0")
}

val bintrayUploadEnabled = System.getenv("bintray_key") != null
val artifactoryUploadEnabled = System.getenv("artifactory_url") != null

publishJar {
    if (bintrayUploadEnabled) {
        bintray {
            username = "tanvd"
            repository = "konfy"
            info {
                description = "Konfy TOML support"
                githubRepo = "https://github.com/TanVD/konfy"
                vcsUrl = "https://github.com/TanVD/konfy"
                labels.addAll(listOf("kotlin", "configuration", "kotlin-dsl", "konfy", "toml"))
            }
        }
    }

    if (artifactoryUploadEnabled) {
        artifactory {
            secretKey = System.getenv("artifactory_api_key")
        }
    }
}


tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
    }
}
