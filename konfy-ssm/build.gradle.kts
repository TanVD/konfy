group = rootProject.group
version = rootProject.version

dependencies {
    compile(project(":konfy"))

    compile("com.amazonaws", "aws-java-sdk-ssm", "1.11.555")
}
