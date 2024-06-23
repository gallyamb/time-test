plugins {
    `java-platform`

    id("io.github.gallyamb.time.base")
    id("io.github.gallyamb.time.publishing")
}

javaPlatform {
    allowDependencies() // Use existing Platforms/BOMs
}

publishing {
    publications {
        named<MavenPublication>(project.name) {
            from(components["javaPlatform"])
        }
    }
}
