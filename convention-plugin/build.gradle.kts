/*
 * Copyright 2025 Aarav Mahajan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    id("conventions.kotlin")
    id("conventions.quality")
    alias(libs.plugins.plugin.publish)
}

description = "Gradle plugin that provides conventions for developing Jenkins plugins"

group = project.property("group") as String
version = project.property("version") as String

// integration test setup
sourceSets {
    create("integrationTest") {
        kotlin {
            compileClasspath += sourceSets.main.get().output
            runtimeClasspath += sourceSets.main.get().output
        }
    }
}

val integrationTestImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

val integrationTestRuntimeOnly by configurations.getting

dependencies {
    compileOnly(gradleApi())
    compileOnly(gradleKotlinDsl())
    compileOnly(libs.kotlin.gradle.plugin) {
        exclude("org.jetbrains.kotlin", "kotlin-compiler-embeddable")
    }
    implementation(libs.jenkins.gradle.jpi2)
    implementation(libs.spotless.gradle.plugin)
    implementation(libs.detekt.gradle.plugin) {
        exclude("org.jetbrains.kotlin", "kotlin-compiler-embeddable")
    }
    implementation(libs.spotbugs.gradle.plugin)
    implementation(libs.owasp.depcheck.gradle.plugin)
    implementation(libs.benmanes.versions.gradle.plugin)
    implementation(libs.pit.gradle.plugin)
    implementation(libs.kover.gradle.plugin)
    implementation(libs.node.gradle.plugin)
    implementation(libs.dokka.gradle.plugin)

    implementation(libs.ktlint.gradle.plugin) {
        exclude("org.jetbrains.kotlin", "kotlin-compiler-embeddable")
    }

    // integration test dependencies
    integrationTestImplementation(gradleTestKit())
    integrationTestImplementation(libs.junit.gradle.plugin)
    integrationTestImplementation(libs.kotest.gradle.plugin)
    integrationTestRuntimeOnly(libs.junit.launcher.gradle.plugin)
    implementation(kotlin("test"))
}

gradlePlugin {
    website = "https://github.com/aaravmahajanofficial/jenkins-gradle-convention-plugin"
    vcsUrl = "https://github.com/aaravmahajanofficial/jenkins-gradle-convention-plugin"
    plugins {
        create("jenkinsConventions") {
            id = "io.github.aaravmahajanofficial.jenkins-gradle-convention-plugin"
            displayName = "Jenkins Gradle Convention Plugin"
            description = "Convention plugin for developing Jenkins plugins with Gradle"
            tags =
                listOf(
                    "jenkins",
                    "convention-plugin",
                    "jenkins-plugin-development",
                    "build-automation",
                    "standardization",
                    "build-logic",
                    "version-catalog",
                )
            implementationClass = "JenkinsConventionPlugin"
        }
    }
}

val integrationTest by tasks.registering(Test::class) {

    systemProperty("kotlin.incremental.compilation", "false")
    systemProperty("kotlin.incremental", "false")

    description = "Runs integration tests."
    group = "Verification"

    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
    shouldRunAfter("test")

    // plugin metadata
    val pluginMetadata = tasks.named("pluginUnderTestMetadata")
    dependsOn(pluginMetadata)
    classpath += files(pluginMetadata.get().outputs.files)

    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
        exceptionFormat = TestExceptionFormat.FULL
        showCauses = true
        showExceptions = true
        showStackTraces = true
    }
}

tasks.check {
    dependsOn(integrationTest)
}

tasks.register("publishToLocal") {
    dependsOn("publishToMavenLocal")
}

tasks.named("build") {
    finalizedBy("publishToLocal")
}
