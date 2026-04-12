plugins {
	id("reason-kotlin")
}

dependencies {
	implementation(projects.playerAttributes)
	api(projects.kronosServer)
}
