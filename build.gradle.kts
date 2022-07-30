val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.7.10"
}

group = "dsp.xmlcompare"
version = "0.0.1"
application {
    mainClass.set("ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.xmlunit:xmlunit-core:2.9.0")
    implementation("com.github.ajalt.clikt:clikt:3.5.0")}
