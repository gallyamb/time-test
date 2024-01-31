plugins {
    id("io.github.gallyamb.time.java-library")
}

dependencies {
    api(project(":core"))
    testImplementation(testFixtures(project(":core-test")))
}
