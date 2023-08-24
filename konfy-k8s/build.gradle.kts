group = rootProject.group
version = rootProject.version

dependencies {
    api(project(":konfy"))
    api("io.kubernetes", "client-java", "18.0.1")
}
