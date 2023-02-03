group = rootProject.group
version = rootProject.version

dependencies {
    api(project(":konfy"))

    api("org.tomlj", "tomlj", "1.1.0")

    testImplementation("org.assertj", "assertj-core", "3.24.2")
    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.9.2")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.9.2")
}


tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
    }
}
