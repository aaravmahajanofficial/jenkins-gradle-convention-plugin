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
package model

import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import java.net.URI
import javax.inject.Inject

public abstract class JenkinsPluginDeveloper
    @Inject
    constructor() {
        public abstract val id: Property<String>
        public abstract val name: Property<String>
        public abstract val email: Property<String>
        public abstract val portfolioUrl: Property<URI>
        public abstract val organization: Property<String>
        public abstract val organizationUrl: Property<URI>
        public abstract val roles: SetProperty<String>
        public abstract val timezone: Property<String>

        init {
            roles.convention(emptySet())
            timezone.convention("UTC")
        }
    }
