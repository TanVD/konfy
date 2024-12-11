group = rootProject.group
version = rootProject.version

dependencies {
    api(project(":konfy"))

    api("software.amazon.awssdk", "ssm", "2.25.13")
    implementation("org.slf4j", "slf4j-api", "2.0.16")

    testImplementation("org.testng", "testng", "7.10.2")
    testImplementation("org.assertj", "assertj-core", "3.26.3")
    testImplementation("io.mockk", "mockk", "1.13.13")
    testImplementation("ch.qos.logback:logback-classic:1.5.12")
}

tasks.test {
    useTestNG()
}

