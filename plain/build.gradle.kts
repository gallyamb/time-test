plugins {
    id("org.time.test.java-library")
}

dependencies {
    api(project(":core"))
    testImplementation(testFixtures(project(":core-test")))
}
