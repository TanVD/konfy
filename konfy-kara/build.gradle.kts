import tanvd.kosogor.proxy.publishJar

group = rootProject.group
version = rootProject.version

dependencies {
    compile(project(":konfy"))
}

publishJar {
    bintray {
        username = "tanvd"
        repository = "konfy"
        info {
            description = "Konfy Kara support"
            githubRepo = "https://github.com/TanVD/konfy"
            vcsUrl = "https://github.com/TanVD/konfy"
            labels.addAll(listOf("kotlin", "configuration", "kotlin-dsl", "konfy", "kara"))
        }
    }
}
