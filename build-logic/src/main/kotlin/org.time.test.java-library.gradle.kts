plugins {
    id("java-library")
    id("org.gradlex.java-module-dependencies")
    id("org.gradlex.java-module-testing")
}

group = "org.time.test"
version = "1.0.0-SNAPSHOT"

configurations {
    create("testOutput")
}

task("jarTest", Jar::class) {
    from(sourceSets.test.get().output)
    archiveClassifier.set("test")
}

artifacts {
    add("testOutput", tasks.named("jarTest"))
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-params")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
