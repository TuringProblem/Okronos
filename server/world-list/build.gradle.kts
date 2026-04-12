plugins {
	id("reason-kotlin")
}

dependencies {
	implementation(projects.kronosServer)

	implementation(libs.ktor.server.core.jvm)
	implementation(libs.ktor.server.netty.jvm)
}
