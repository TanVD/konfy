group = rootProject.group
version = rootProject.version

dependencies {
    compile(project(":konfy"))

    compile("net.consensys.cava", "cava-toml", "0.6.0")
}
