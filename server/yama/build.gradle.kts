plugins {
    id("reason-kotlin")
}
repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.coreCombatApiReason)
    implementation(projects.playerAttributesApi)
}