import tanvd.kosogor.proxy.publishJar

group = rootProject.group
version = rootProject.version

dependencies {
    compile(project(":konfy"))

    compile("de.slackspace", "openkeepass", "0.8.2")
}

publishJar {
    publication {
        artifactId = "tanvd.konfy.keepass"
    }

    bintray {
        username = "tanvd"
        repository = "konfy"
        info {
            description = "Konfy Keepass support"
            githubRepo = "https://github.com/TanVD/konfy"
            vcsUrl = "https://github.com/TanVD/konfy"
            labels.addAll(listOf("kotlin", "configuration", "kotlin-dsl", "konfy", "keepass"))
        }
    }
}
