import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import tanvd.kosogor.proxy.publishJar

group = "tanvd.konfy"
version = "0.1.20-SNAPSHOT"

plugins {
    id("tanvd.kosogor") version "1.0.16" apply true
    id("io.gitlab.arturbosch.detekt") version ("1.17.1") apply true
    kotlin("jvm") version "1.7.20" apply false
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

    tasks.withType<KotlinJvmCompile>().forEach {
        it.kotlinOptions {
            jvmTarget = "11"
            languageVersion = "1.7"
            apiVersion = "1.7"
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
