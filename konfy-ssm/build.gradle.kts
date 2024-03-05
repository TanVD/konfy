group = rootProject.group
version = rootProject.version

dependencies {
    api(project(":konfy"))

    api("com.amazonaws", "aws-java-sdk-ssm", "1.12.655")
    implementation("org.slf4j:slf4j-api:1.7.36")

    testImplementation("org.testng", "testng", "7.9.0")
    testImplementation("org.assertj", "assertj-core", "3.25.3")
    testImplementation("io.mockk", "mockk", "1.13.9")
    testImplementation("ch.qos.logback:logback-classic:1.5.0")
}

tasks.test {
    useTestNG()
}

