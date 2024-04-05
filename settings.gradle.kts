pluginManagement {
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    repositories.mavenCentral()
}

rootProject.name = "time-test"

val filesWithinLibsDirectory: Array<File> = file("libs").listFiles() ?: arrayOf()

filesWithinLibsDirectory.filter { it.isDirectory }
    .map { it.name }
    .forEach {
        include(it)
        project(":$it").projectDir = file("libs/$it")
    }

include("platform")
