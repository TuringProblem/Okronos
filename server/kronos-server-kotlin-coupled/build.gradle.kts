plugins {
	id("reason-kotlin")
}

dependencies {
	api(projects.kronosServer)
	api(libs.kotlinx.coroutines.core.jvm)
}
