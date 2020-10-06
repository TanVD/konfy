import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

group = "tanvd.konfy"
version = "0.1.17"

plugins {
    id("tanvd.kosogor") version "1.0.9" apply true
    id("io.gitlab.arturbosch.detekt") version ("1.8.0") apply true
    kotlin("jvm") version "1.3.72" apply false
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

    tasks.withType<KotlinJvmCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            languageVersion = "1.3"
            apiVersion = "1.3"
        }
    }

    detekt {
        parallel = true

        config = rootProject.files("detekt.yml")

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
