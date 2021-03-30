import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

group = "tanvd.konfy"
version = "0.1.19"

plugins {
    id("tanvd.kosogor") version "1.0.10" apply true
    id("io.gitlab.arturbosch.detekt") version ("1.15.0-RC1") apply true
    kotlin("jvm") version "1.4.32" apply false
}

val bintrayUploadEnabled = System.getenv("bintray_key") != null
val artifactoryUploadEnabled = System.getenv("artifactory_url") != null

subprojects {
    apply {
        plugin("kotlin")
        plugin("tanvd.kosogor")
        plugin("io.gitlab.arturbosch.detekt")
    }

    repositories {
        mavenCentral()
        if (bintrayUploadEnabled)
            jcenter()
        if (artifactoryUploadEnabled)
            maven(System.getenv("artifactory_url")!!)
    }

    tasks.withType<KotlinJvmCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            languageVersion = "1.4"
            apiVersion = "1.4"
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
