group = rootProject.group
version = rootProject.version

dependencies {
    api(project(":konfy"))

    api("org.tomlj", "tomlj", "1.1.1")

    testImplementation("org.assertj", "assertj-core", "3.26.3")
    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.11.3")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.11.3")
}


tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
    }
}
