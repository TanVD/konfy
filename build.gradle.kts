import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import tanvd.kosogor.proxy.publishJar

group = "tanvd.konfy"
version = "0.1.28"

plugins {
    id("tanvd.kosogor") version "1.0.18" apply false
    id("io.gitlab.arturbosch.detekt") version ("1.22.0") apply true
    kotlin("jvm") version "1.9.22" apply false
    `kotlin-dsl`
    `maven-publish`
}

subprojects {
    apply {
        plugin("kotlin")
        plugin("maven-publish")
        plugin("tanvd.kosogor")
        plugin("io.gitlab.arturbosch.detekt")
    }

    repositories {
        mavenCentral()
    }

    publishJar {  }

    publishing {
        repositories {
            maven {
                name = "SpacePackages"
                url = uri("https://packages.jetbrains.team/maven/p/konfy/maven")

                credentials {
                    username = System.getenv("JB_SPACE_CLIENT_ID")
                    password = System.getenv("JB_SPACE_CLIENT_SECRET")
                }
            }
        }
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    tasks.withType<KotlinJvmCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "17"
            apiVersion = "1.9"
            languageVersion = "1.9"
            freeCompilerArgs += "-Xuse-ir"
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
