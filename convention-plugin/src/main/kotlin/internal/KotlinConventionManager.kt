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
package internal

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

private const val JAVA_VERSION = 17

public class KotlinConventionManager(
    private val project: Project,
    private val libs: VersionCatalog,
) {
    public fun configure() {
        project.plugins.withId("org.jetbrains.kotlin.jvm") {
            project.configure<KotlinJvmProjectExtension> {
                jvmToolchain(JAVA_VERSION)
//                explicitApi()
            }

            project.tasks.withType<KotlinCompile>().configureEach { t ->
                t.compilerOptions {
                    languageVersion.set(KotlinVersion.KOTLIN_2_2)
                    apiVersion.set(KotlinVersion.KOTLIN_2_2)
                    jvmTarget.set(JvmTarget.JVM_17)
                    allWarningsAsErrors.set(true)
                    freeCompilerArgs.addAll(
                        "-Xjsr305=strict",
                        "-opt-in=kotlin.RequiresOptIn",
                    )
                }
            }

            project.dependencies {
                add("compileOnly", platform(libs.findLibrary("kotlin-bom").get()))
                add("compileOnly", libs.findLibrary("kotlin-stdlib").get())
                add("compileOnly", libs.findLibrary("kotlin-reflect").get())

                add("compileOnly", libs.findLibrary("jetbrains-annotations").get())
            }
        }
    }
}
