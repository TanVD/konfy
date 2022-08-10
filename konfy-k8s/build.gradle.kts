import tanvd.kosogor.proxy.publishJar

group = rootProject.group
version = rootProject.version

dependencies {
    api(project(":konfy"))
    api("io.kubernetes", "client-java", "15.0.1")
}
