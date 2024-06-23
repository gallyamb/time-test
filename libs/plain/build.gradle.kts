plugins {
    id("io.github.gallyamb.time.java-library")
}

dependencies {
    api(project(":time-core"))
    testImplementation(testFixtures(project(":time-test-core")))
}
