group = rootProject.group
version = rootProject.version

dependencies {
    api(project(":konfy"))

    api("software.amazon.awssdk", "ssm", "2.25.13")
    implementation("org.slf4j", "slf4j-api", "1.7.36")

    testImplementation("org.testng", "testng", "7.9.0")
    testImplementation("org.assertj", "assertj-core", "3.25.3")
    testImplementation("io.mockk", "mockk", "1.13.9")
    testImplementation("ch.qos.logback:logback-classic:1.5.0")
}

tasks.test {
    useTestNG()
}

