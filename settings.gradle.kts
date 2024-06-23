pluginManagement {
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "time-test"

include("platform")
includeProjects("libs")

////////////////////////////////////////////////////
//                 Extra functions                //
////////////////////////////////////////////////////

fun includeProjects(projectsRoot: String) {
    val filesWithinRootDirectory: Array<File> = file(projectsRoot).listFiles() ?: arrayOf()

    filesWithinRootDirectory.filter { it.isDirectory }
        .map { it.name }
        .forEach { projectName ->
            include(projectName)
            project(":$projectName").apply {
                projectDir = file("$projectsRoot/$projectName")

                name = when (name) {
                    "core" -> "time-core"
                    "core-test" -> "time-test-core"
                    else -> "time-test-${name}"
                }

                require(buildFile.isFile) {
                    "Build file $buildFile for project $name does not exist."
                }

            }
        }
}
