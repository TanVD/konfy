group = rootProject.group
version = rootProject.version

dependencies {
    api(project(":konfy"))

    api("de.slackspace", "openkeepass", "0.8.2") {
        exclude("org.simpleframework", "simple-xml")
    }
    api("com.carrotsearch.thirdparty", "simple-xml-safe", "2.7.1")
}


