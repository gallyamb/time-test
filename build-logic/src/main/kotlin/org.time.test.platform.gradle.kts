plugins {
    id("java-platform")
    id("org.time.test.base")
}

javaPlatform {
    allowDependencies() // Use existing Platforms/BOMs
}
