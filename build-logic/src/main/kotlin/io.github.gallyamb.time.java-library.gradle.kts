plugins {
    `java-library`
    `java-test-fixtures`
    `maven-publish`

    id("io.github.gallyamb.time.base")
}

publishing {
    publications {
        create<MavenPublication>(project.name) {
            artifactId = "time-${if (project.name == "core") "" else "test-"}${project.name}"
            from(components["java"])
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

java {
    withSourcesJar()
    withJavadocJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    api(platform(project(":platform")))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testFixturesApi(platform(project(":platform")))
    testFixturesImplementation("org.junit.jupiter:junit-jupiter")
    testFixturesRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
