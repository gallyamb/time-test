plugins {
    id("io.github.gallyamb.time.java-library")
}

dependencies {
    implementation(project(":time-core"))
    testImplementation(project(":time-test-plain"))
    testImplementation(testFixtures(project(":time-test-core")))

    implementation("org.junit.platform:junit-platform-launcher")
}
