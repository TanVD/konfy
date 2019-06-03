group = "tanvd.konfy"
version = "0.1.6"

plugins {
    id("tanvd.kosogor") version "1.0.5" apply true
    id("io.gitlab.arturbosch.detekt").version("1.0.0-RC14") apply true
    kotlin("jvm") version "1.3.31" apply false
}


subprojects {
    apply {
        plugin("kotlin")
        plugin("tanvd.kosogor")
        plugin("io.gitlab.arturbosch.detekt")
    }

    repositories {
        jcenter()
    }

    detekt {
        parallel = true
        failFast = false
        config = files(File(project.rootProject.projectDir, "buildScripts/detekt/detekt.yml"))
        reports {
            xml {
                enabled = false
            }
            html {
                enabled = false
            }
        }
    }
}
