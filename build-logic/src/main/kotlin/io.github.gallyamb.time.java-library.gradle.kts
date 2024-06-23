plugins {
    `java-library`
    `java-test-fixtures`

    id("io.github.gallyamb.time.base")
    id("io.github.gallyamb.time.publishing")
}

java {
    withSourcesJar()
    withJavadocJar()
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks {
    test {
        useJUnitPlatform()
    }

    javadoc {
        val opt = options
        if (opt is CoreJavadocOptions) {
            opt.addBooleanOption("html5", true)
        }
    }
}

publishing {
    publications {
        named<MavenPublication>(project.name) {
            from(components["java"])
        }
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
