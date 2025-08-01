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

import io.github.aaravmahajanofficial.extensions.PluginExtension
import org.eclipse.jgit.api.Git
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.newInstance
import org.jenkinsci.gradle.plugins.jpi.JpiExtension
import org.jenkinsci.gradle.plugins.jpi.core.PluginDeveloper
import org.jenkinsci.gradle.plugins.jpi.core.PluginLicense
import java.nio.file.Paths

public class JpiPluginManager(
    private val project: Project,
    private val pluginExtension: PluginExtension,
) {
    private val jpiExtension: JpiExtension by lazy {
        project.extensions.getByType<JpiExtension>()
    }

    public fun applyAndConfigure() {
        project.pluginManager.apply("org.jenkins-ci.jpi")

        bridgeExtensionProperties()
        project.tasks.findByName("generateLicenseInfo")?.enabled = false
    }

    private fun bridgeExtensionProperties() =
        with(jpiExtension) {
            pluginId.convention(pluginExtension.pluginId)
            humanReadableName.convention(pluginExtension.humanReadableName)
            homePage.convention(pluginExtension.homePage)
            jenkinsVersion.convention(pluginExtension.jenkinsVersion)
            minimumJenkinsCoreVersion.convention(pluginExtension.minimumJenkinsCoreVersion)
            extension.convention(pluginExtension.extension)
            scmTag.convention(pluginExtension.scm.tag)
            gitHub.convention(pluginExtension.gitHub)
            generateTests.convention(pluginExtension.generateTests)
            generatedTestClassName.convention(pluginExtension.generatedTestClassName)
            sandboxed.convention(pluginExtension.sandboxed)
            usePluginFirstClassLoader.convention(pluginExtension.usePluginFirstClassLoader)
            maskedClassesFromCore.convention(pluginExtension.maskedClassesFromCore)
            incrementalsRepoUrl.convention(pluginExtension.incrementalsRepoUrl)
            testJvmArguments.convention(pluginExtension.testJvmArguments)
            requireEscapeByDefaultInJelly.convention(pluginExtension.requireEscapeByDefaultInJelly)

            pluginDevelopers.set(
                pluginExtension.pluginDevelopers.map { developers ->
                    developers.map { dev ->
                        project.objects.newInstance<PluginDeveloper>().apply {
                            id.set(dev.id)
                            name.set(dev.name)
                            email.set(dev.email)
                            url.set(dev.website.toString())
                            organization.set(dev.organization)
                            organizationUrl.set(dev.organizationUrl.toString())
                            roles.set(dev.roles)
                            timezone.set(dev.timezone)
                        }
                    }
                },
            )

            pluginLicenses.set(
                pluginExtension.pluginLicenses.map { licenses ->
                    licenses.map { lic ->
                        project.objects.newInstance<PluginLicense>().apply {
                            name.set(lic.name)
                            url.set(lic.url.toString())
                            distribution.set(lic.distribution)
                            comments.set(lic.comments)
                        }
                    }
                },
            )
        }

    private fun getFullHashFromJpi(): String {
        val repoDir = Paths.get(project.rootDir.absolutePath).toFile()
        return try {
            Git.open(repoDir).use { git ->
                git.repository.resolve("HEAD").name
                    ?: throw IllegalStateException("Cannot resolve HEAD in repo: $repoDir")
            }
        } catch (e: IllegalStateException) {
            throw IllegalStateException("Failed to retrieve Git HEAD SHA in repo: $repoDir", e)
        }
    }
}
