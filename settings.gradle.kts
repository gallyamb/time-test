pluginManagement {
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    repositories.mavenCentral()
}

rootProject.name = "time-test"

include(
    "core",
    "core-test",
    "plain",
    "mockito",
    "junit5-integration"
)

include("platform")
