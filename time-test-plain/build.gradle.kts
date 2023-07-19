plugins {
    id("org.time.test.java-library")
}

dependencies {
    testImplementation(project(":time-test-core", "testOutput"))
}