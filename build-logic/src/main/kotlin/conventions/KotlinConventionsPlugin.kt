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
package conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

private const val JAVA_VERSION = 21

public class KotlinConventionsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply("org.jetbrains.kotlin.jvm")

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            project.configureKotlin(libs)
            project.configureCommonDependencies(libs)
        }
    }
}

private fun Project.configureKotlin(libs: VersionCatalog) {
    configure<KotlinJvmProjectExtension> {
        jvmToolchain(JAVA_VERSION)

        explicitApi()

        val kotlinVersion = libs.findVersion("kotlin").get().requiredVersion.split(".").let { "${it[0]}.${it[1]}" }

        compilerOptions {
            apiVersion.set(KotlinVersion.fromVersion(kotlinVersion))
            languageVersion.set(KotlinVersion.fromVersion(kotlinVersion))
            jvmTarget.set(JvmTarget.JVM_21)

            allWarningsAsErrors.set(true)
            progressiveMode.set(false)

            freeCompilerArgs.addAll(
                "-Xjsr305=strict",
                "-opt-in=kotlin.RequiresOptIn",
                "-Xjvm-default=all",
            )
        }
    }
}

private fun Project.configureCommonDependencies(libs: VersionCatalog) {
    dependencies {
        add("implementation", platform(libs.findLibrary("kotlin-bom").get()))
        add("implementation", libs.findLibrary("kotlin-stdlib").get())
        add("implementation", libs.findLibrary("kotlin-reflect").get())

        add("compileOnly", libs.findLibrary("jetbrains-annotations").get())
    }
}
