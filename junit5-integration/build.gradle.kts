plugins {
    id("io.github.gallyamb.time.java-library")
}

dependencies {
    implementation(project(":core"))
    testImplementation(project(":plain"))
    testImplementation(testFixtures(project(":core-test")))

    implementation("org.junit.platform:junit-platform-launcher")
}
