group = "io.github.gallyamb.time"
version = providers.gradleProperty("releaseVersion")
    .orElse(providers.gradleProperty("version"))
    .get()
