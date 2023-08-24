group = rootProject.group
version = rootProject.version

dependencies {
    api(project(":konfy"))

    api("com.amazonaws", "aws-java-sdk-ssm", "1.12.485")
}


