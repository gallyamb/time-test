plugins { application }
java { toolchain { languageVersion = JavaLanguageVersion.of(17) } }
val timeVersion = providers.gradleProperty("timeTestVersion").get()
dependencies {
    implementation("io.github.gallyamb.time:time-core:$timeVersion")
    implementation("io.github.gallyamb.time:time-test-core:$timeVersion")
    runtimeOnly("io.github.gallyamb.time:time-test-plain:$timeVersion")
}
application { mainClass = "example.ClockConsumer" }
