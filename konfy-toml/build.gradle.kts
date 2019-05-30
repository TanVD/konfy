import tanvd.kosogor.proxy.publishJar

group = rootProject.group
version = rootProject.version

dependencies {
    compile(project(":konfy"))

    compile("net.consensys.cava", "cava-toml", "0.6.0")
}

publishJar {
    publication {
        artifactId = "tanvd.konfy.toml"
    }

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
