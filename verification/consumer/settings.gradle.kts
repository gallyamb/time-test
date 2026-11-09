rootProject.name = "time-test-publication-consumer"
dependencyResolutionManagement {
    repositories {
        providers.gradleProperty("timeTestRepository").orNull?.let { repository ->
            maven {
                url = uri(repository)
                content { includeGroup("io.github.gallyamb.time") }
            }
        }
        mavenCentral()
    }
}
