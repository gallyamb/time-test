plugins {
    id("io.github.gallyamb.time.java-library")
}

dependencies {
    api(project(":time-core"))
    testImplementation(testFixtures(project(":time-test-core")))
}

// Exercise service providers on a regular classpath as well as the existing JPMS tests.
val classpathTest = sourceSets.create("classpathTest")
configurations[classpathTest.implementationConfigurationName].extendsFrom(configurations.testImplementation.get())
configurations[classpathTest.runtimeOnlyConfigurationName].extendsFrom(configurations.testRuntimeOnly.get())
classpathTest.compileClasspath += sourceSets.main.get().output
classpathTest.runtimeClasspath += sourceSets.main.get().output
val classpathTestTask = tasks.register<Test>("classpathTest") {
    description = "Tests the plain clock and service discovery outside JPMS."
    group = "verification"
    testClassesDirs = classpathTest.output.classesDirs
    classpath = classpathTest.runtimeClasspath
    modularity.inferModulePath.set(false)
    useJUnitPlatform()
    maxParallelForks = 1
}
tasks.check { dependsOn(classpathTestTask) }
