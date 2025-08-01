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
package io.github.aaravmahajanofficial.extensions

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import java.net.URI
import javax.inject.Inject

public class SCMExtension
    @Inject
    constructor(
        objects: ObjectFactory,
    ) {
        public val connection: Property<String> = objects.property<String>()
        public val developerConnection: Property<String> = objects.property<String>()
        public val tag: Property<String> = objects.property<String>().convention("HEAD")
        public val url: Property<URI> = objects.property()
    }
