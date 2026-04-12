plugins {
	id("reason-kotlin")
}

dependencies {
	implementation(projects.playerAttributesApi)
	implementation(projects.friendlist)
	implementation(projects.kronosServer)
	implementation(projects.coreDatabase)
}
