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
package io.github.aaravmahajanofficial.internal

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

private const val JAVA_VERSION = 21

public class JavaConventionManager(
    private val project: Project,
) {
    public fun configure() {
        project.pluginManager.apply("java")

        project.configure<JavaPluginExtension> {
            toolchain.languageVersion.set(JavaLanguageVersion.of(JAVA_VERSION))
            withSourcesJar()
            withJavadocJar()
        }

        project.tasks.withType<JavaCompile>().configureEach {
            it.options.encoding = "UTF-8"
            it.options.release.set(JAVA_VERSION)
            it.options.compilerArgs.addAll(
                listOf(
                    "-parameters",
                    "-Xlint:all,-serial",
                ),
            )
        }

        project.tasks.withType<AbstractArchiveTask> {
            isPreserveFileTimestamps = false
            isReproducibleFileOrder = true
        }
    }
}
