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
            val project = project(":$projectName")

            project.projectDir = file("$projectsRoot/$projectName")

            require(project.buildFile.isFile) {
                "Build file ${project.buildFile} for project ${project.name} does not exist."
            }
        }
}
