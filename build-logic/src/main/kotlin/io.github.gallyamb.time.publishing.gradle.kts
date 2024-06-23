import java.nio.file.Files
import java.nio.file.Paths

plugins {
    `maven-publish`
    signing

    id("io.github.gallyamb.time.base")
}

publishing {
    publications {
        create<MavenPublication>(project.name) {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            pom {
                name = project.name
                description = "Java library for convenient testing of time sensitive code"
                url = "https://gallyamb.github.io/time-test/"
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                developers {
                    developer {
                        id = "gallyamb"
                        name = "Gallyam Biktashev"
                        email = "gallyamb@gmail.com"
                    }
                }
                scm {
                    connection = "scm:git:git@github.com:gallyamb/time-test.git"
                    developerConnection = "scm:git:git@github.com:gallyamb/time-test.git"
                    url = "https://github.com/gallyamb/time-test"
                }
            }
        }
    }

    if (isCI()) {
        repositories {
            maven {
                name = "OSSRH"
                url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = findStringProperty("OSSRH_USERNAME")
                    password = findStringProperty("OSSRH_TOKEN")
                }
            }
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/gallyamb/time-test")
                credentials {
                    username = findStringProperty("GITHUB_ACTOR")
                    password = findStringProperty("GITHUB_TOKEN")
                }
            }
        }
    }
}

signing {
    if (!isCI()) {
        return@signing
    }
    val signingKeyId = findStringProperty("SIGNING_KEY_ID")
    val signingPassword = findStringProperty("SIGNING_PASSWORD")

    val signingKeyFile = findStringProperty("SIGNING_KEY")
    val signingKey = Files.readString(Paths.get(signingKeyFile))

    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)

    sign(publishing.publications[project.name])
}


////////////////////////////////////////////////////
//                 Extra functions                //
////////////////////////////////////////////////////

fun findStringProperty(key: String): String {
    return System.getenv(key) ?: ""
}

fun isCI(): Boolean {
    return findStringProperty("IS_CI") == "true"
}
