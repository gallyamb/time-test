plugins {
    id("org.time.test.platform")
}

dependencies {
    api(platform("org.junit:junit-bom:5.9.1"))
    api(platform("org.mockito:mockito-bom:5.8.0"))
}
