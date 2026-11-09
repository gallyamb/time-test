plugins {
    `maven-publish`
    id("com.vanniktech.maven.publish.base")

    id("io.github.gallyamb.time.base")
}

publishing {
    publications {
        create<MavenPublication>(project.name) {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            pom {
                name = project.name
                description = "Java library for convenient testing of time sensitive code"
                url = "https://gallyamb.github.io/time-test/"
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                developers {
                    developer {
                        id = "gallyamb"
                        name = "Gallyam Biktashev"
                        email = "gallyamb@gmail.com"
                    }
                }
                scm {
                    connection = "scm:git:git@github.com:gallyamb/time-test.git"
                    developerConnection = "scm:git:git@github.com:gallyamb/time-test.git"
                    url = "https://github.com/gallyamb/time-test"
                }
            }
        }
    }

    repositories {
        maven {
            name = "localStaging"
            url = rootProject.layout.buildDirectory.dir("staging-repository").get().asFile.toURI()
        }
    }
}

val releaseRequested = providers.gradleProperty("release").map(String::toBoolean).orElse(false)
val signArtifacts = providers.gradleProperty("signArtifacts").map(String::toBoolean).orElse(false)
mavenPublishing {
    publishToMavenCentral()
    if (releaseRequested.get() || signArtifacts.get()) {
        signAllPublications()
    }
}

val validateCentralRelease = tasks.register("validateCentralRelease") {
    group = "verification"
    description = "Checks release identity and required publishing configuration without exposing secrets."
    doLast {
        require(releaseRequested.get()) { "Central publishing requires -Prelease=true" }
        val releaseVersion = project.version.toString()
        require(Regex("[0-9]+\\.[0-9]+\\.[0-9]+(?:-[A-Za-z0-9.-]+)?").matches(releaseVersion)
            && !releaseVersion.endsWith("-SNAPSHOT")) { "A fixed releaseVersion is required" }
        require(providers.environmentVariable("RELEASE_TAG").orNull == "v$releaseVersion") {
            "RELEASE_TAG must match v$releaseVersion"
        }
        listOf("mavenCentralUsername", "mavenCentralPassword", "signingInMemoryKey").forEach { key ->
            require(!providers.gradleProperty(key).orNull.isNullOrBlank()) { "Missing release property: $key" }
        }
    }
}

tasks.withType<PublishToMavenRepository>().configureEach {
    if (repository.name == "mavenCentral") {
        dependsOn(validateCentralRelease)
    }
}
