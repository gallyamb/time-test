plugins {
    `java-library`
    `java-test-fixtures`
    `maven-publish`
    signing

    id("io.github.gallyamb.time.base")
}

java {
    withSourcesJar()
    withJavadocJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
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
        create<MavenPublication>(project.name) {
            groupId = project.group.toString()
            artifactId = when (project.name) {
                "core" -> "time-core"
                "core-test" -> "time-test-core"
                else -> "time-test-${project.name}"
            }
            version = project.version.toString()
            from(components["java"])
        }
    }

    if (isCI()) {
        repositories {
            maven {
                name = "OSSRH"
                url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = System.getenv("OSSRH_USERNAME")
                    password = System.getenv("OSSRH_TOKEN")
                }
            }
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/gallyamb/time-test")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}

signing {
    if (isCI()) {
        val signingKeyId = findStringProperty("signingKeyId")
        val signingKey = findStringProperty("signingKey")
        val signingPassword = findStringProperty("signingPassword")
        useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    } else {
        useGpgCmd()
    }
    sign(publishing.publications[project.name])
}

dependencies {
    api(platform(project(":platform")))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testFixturesApi(platform(project(":platform")))
    testFixturesImplementation("org.junit.jupiter:junit-jupiter")
    testFixturesRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


////////////////////////////////////////////////////
//                 Extra functions                //
////////////////////////////////////////////////////

fun findStringProperty(key: String): String? {
    return findProperty(key)?.toString()
}

fun isCI(): Boolean {
    return findStringProperty("CI") == "true"
}
