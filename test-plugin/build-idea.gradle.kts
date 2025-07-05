import extensions.JenkinsPluginExtension

plugins {
    id("io.jenkins.gradle.convention") version "1.0.0"
}

group = "org.jenkins-ci.plugins"
artifactId = "checkmarx"
version = "1.0.0-SNAPSHOT"
description = "Checkmarx CxSAST Plugin for Jenkins"

jenkinsConvention {   
    humanReadableName.set("Jenkins Checkmarx Plugin")
    
    homePage.set(uri("https://wiki.jenkins-ci.org/display/JENKINS/Checkmarx+CxSAST+Plugin"))
    githubUrl.set(uri("https://github.com/jenkinsci/checkmarx-plugin.git"))
    jenkinsVersion.set("2.516")
    minimumJenkinsCoreVersion.set("2.516")
    usePluginFirstClassLoader.set(true)
    generateTests.set(false)
    pipelineCompatible.set(true)
    pluginType.set(JenkinsPluginExtension.PluginType.BUILD) // Default is MISC
    
    labels = ["security", "static-analysis"],

    developer {
        id.set("am12")
        name.set("Aarav Mahajan")
        // timezone is already set to UTC
        portfolioUrl.set(uri("https://github.com/aaravmahajanofficial/jenkins-gradle-convention-plugin"))
        roles.set(setOf("dev", "contributor"))
        email.set("aaravmahajan2003@gmail.com")
        organization.set("GSoC, Kotlin Foundation")
    }

    features {
      // When enabled, injects BOM and proper dependencies
      core = true, 
      mockito = true,
      testcontainers = true
    }
}

jenkinsPublishing {
  jenkinPluginPortal()
  maven {
    ...
  }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Project specific BOM-managed dependencies
    // Non-BOM dependencies
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.8.0.202311291450-r")
    implementation("org.codehaus.plexus:plexus-utils:3.5.1")
    implementation("org.iq80.snappy:snappy:0.5")
    compileOnly("com.intellij:annotations:12.0")
    testImplementation("org.eclipse.sisu:org.eclipse.sisu.plexus:0.0.0.M5")
    testImplementation("org.jmockit:jmockit:1.16")

    optionalJenkinsPlugins(
        "org.jenkins-ci.main:maven-plugin:3.4@jar",
        "org.jenkins-ci.plugins:credentials:1381.v2c3a_12074da_b_@jar"
    )
}
