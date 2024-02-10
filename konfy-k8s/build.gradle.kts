group = rootProject.group
version = rootProject.version

dependencies {
    api(project(":konfy"))
    api("io.kubernetes", "client-java", "20.0.0")
}
