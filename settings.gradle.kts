pluginManagement {
    includeBuild("./build-logic")
}

dependencyResolutionManagement {
    repositories.mavenCentral()
}

rootProject.name = "time-test"

include(
    "time-test-core",
    "time-test-plain"
)
