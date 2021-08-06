group = rootProject.group
version = rootProject.version

dependencies {
    api(project(":konfy"))

    api("org.tomlj", "tomlj", "1.0.0")

    testImplementation("org.assertj", "assertj-core", "3.11.1")
    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.2.0")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.2.0")
}


tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
    }
}
