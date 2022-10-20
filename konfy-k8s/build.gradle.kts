group = rootProject.group
version = rootProject.version

dependencies {
    api(project(":konfy"))
    api("io.kubernetes", "client-java", "16.0.0") {
        exclude("org.yaml", "snakeyaml")
        api("org.yaml", "snakeyaml", "1.33")
    }
}
