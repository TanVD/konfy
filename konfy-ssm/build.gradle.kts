import tanvd.kosogor.proxy.publishJar

group = rootProject.group
version = rootProject.version

dependencies {
    compile(project(":konfy"))

    compile("com.amazonaws", "aws-java-sdk-ssm", "1.11.555")
}

publishJar {
    publication {
        artifactId = "tanvd.konfy.ssm"
    }

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

