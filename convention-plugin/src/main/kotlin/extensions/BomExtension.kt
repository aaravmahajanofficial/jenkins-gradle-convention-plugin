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
package extensions

import constants.ConfigurationConstants
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.mapProperty
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

public abstract class BomExtension
    @Inject
    constructor(
        private val project: Project,
        libs: VersionCatalog,
    ) {
        private val objects: ObjectFactory = project.objects

        private fun <T : Any> gradleProperty(
            key: String,
            converter: (String) -> T,
        ): Provider<T> = project.providers.gradleProperty(key).map(converter)

        private fun gradleProperty(key: String) = project.providers.gradleProperty(key)

        // Jenkins BOM
        public val useCoreBom: Property<Boolean> =
            objects.property<Boolean>().convention(
                gradleProperty(
                    ConfigurationConstants.USE_CORE_BOM,
                    String::toBoolean,
                ).orElse(true),
            )
        public val bomVersion: Property<String> =
            objects.property<String>().convention(
                gradleProperty(ConfigurationConstants.CORE_BOM_VERSION).orElse(
                    libs
                        .findVersion("jenkins-bom")
                        .get()
                        .requiredVersion,
                ),
            )

        // Ecosystem BOM
        public val useCommonBoms: Property<Boolean> = objects.property<Boolean>().convention(true)

        public val useGroovyBom: Property<Boolean> =
            objects.property<Boolean>().convention(
                gradleProperty(
                    ConfigurationConstants.USE_GROOVY_BOM,
                    String::toBoolean,
                ).orElse(true),
            )
        public val groovyBomVersion: Property<String> =
            objects.property<String>().convention(
                gradleProperty(ConfigurationConstants.GROOVY_BOM_VERSION).orElse(
                    libs
                        .findVersion("groovy-bom")
                        .get()
                        .requiredVersion,
                ),
            )

        public val useJacksonBom: Property<Boolean> =
            objects.property<Boolean>().convention(
                gradleProperty(
                    ConfigurationConstants.USE_JACKSON_BOM,
                    String::toBoolean,
                ).orElse(true),
            )
        public val jacksonBomVersion: Property<String> =
            objects.property<String>().convention(
                gradleProperty(ConfigurationConstants.JACKSON_BOM_VERSION).orElse(
                    libs
                        .findVersion("jackson-bom")
                        .get()
                        .requiredVersion,
                ),
            )

        public val useSpringBom: Property<Boolean> =
            objects.property<Boolean>().convention(
                gradleProperty(
                    ConfigurationConstants.USE_SPRING_BOM,
                    String::toBoolean,
                ).orElse(true),
            )
        public val springBomVersion: Property<String> =
            objects.property<String>().convention(
                gradleProperty(ConfigurationConstants.SPRING_BOM_VERSION).orElse(
                    libs
                        .findVersion("spring-bom")
                        .get()
                        .requiredVersion,
                ),
            )

        public val useNettyBom: Property<Boolean> =
            objects.property<Boolean>().convention(
                gradleProperty(
                    ConfigurationConstants.USE_NETTY_BOM,
                    String::toBoolean,
                ).orElse(true),
            )
        public val nettyBomVersion: Property<String> =
            objects.property<String>().convention(
                gradleProperty(ConfigurationConstants.NETTY_BOM_VERSION).orElse(
                    libs
                        .findVersion("netty-bom")
                        .get()
                        .requiredVersion,
                ),
            )

        public val useSlf4jBom: Property<Boolean> =
            objects.property<Boolean>().convention(
                gradleProperty(
                    ConfigurationConstants.USE_SLF4J_BOM,
                    String::toBoolean,
                ).orElse(true),
            )
        public val slf4jBomVersion: Property<String> =
            objects.property<String>().convention(
                gradleProperty(ConfigurationConstants.SLF4J_BOM_VERSION).orElse(
                    libs
                        .findVersion("slf4j-bom")
                        .get()
                        .requiredVersion,
                ),
            )

        public val useJettyBom: Property<Boolean> =
            objects.property<Boolean>().convention(
                gradleProperty(
                    ConfigurationConstants.USE_JETTY_BOM,
                    String::toBoolean,
                ).orElse(true),
            )
        public val jettyBomVersion: Property<String> =
            objects.property<String>().convention(
                gradleProperty(ConfigurationConstants.JETTY_BOM_VERSION).orElse(
                    libs
                        .findVersion("jetty-bom")
                        .get()
                        .requiredVersion,
                ),
            )

        // Testing BOM
        public val useJunitBom: Property<Boolean> =
            objects.property<Boolean>().convention(
                gradleProperty(
                    ConfigurationConstants.USE_JUNIT_BOM,
                    String::toBoolean,
                ).orElse(true),
            )
        public val junitBomVersion: Property<String> =
            objects.property<String>().convention(
                gradleProperty(ConfigurationConstants.JUNIT_BOM_VERSION).orElse(
                    libs
                        .findVersion("junit-bom")
                        .get()
                        .requiredVersion,
                ),
            )

        public val useMockitoBom: Property<Boolean> =
            objects
                .property<Boolean>()
                .convention(gradleProperty(ConfigurationConstants.USE_MOCKITO_BOM, String::toBoolean).orElse(true))
        public val mockitoBomVersion: Property<String> =
            objects.property<String>().convention(
                gradleProperty(ConfigurationConstants.MOCKITO_BOM_VERSION).orElse(
                    libs
                        .findVersion("mockito-bom")
                        .get()
                        .requiredVersion,
                ),
            )

        public val useTestcontainersBom: Property<Boolean> =
            objects.property<Boolean>().convention(
                gradleProperty(
                    ConfigurationConstants.USE_TESTCONTAINERS_BOM,
                    String::toBoolean,
                ).orElse(true),
            )
        public val testcontainersBomVersion: Property<String> =
            objects.property<String>().convention(
                gradleProperty(ConfigurationConstants.TESTCONTAINERS_BOM_VERSION).orElse(
                    libs
                        .findVersion("testcontainers-bom")
                        .get()
                        .requiredVersion,
                ),
            )

        public val customBoms: MapProperty<String, String> = objects.mapProperty<String, String>()
    }
