plugins {
    id("java-platform")
    id("io.github.gallyamb.time.base")
}

javaPlatform {
    allowDependencies() // Use existing Platforms/BOMs
}
