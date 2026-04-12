plugins {
	id("reason-kotlin")
}

dependencies {
	api(libs.slf4j.api)

	implementation(projects.properties)
	implementation(libs.logback.core)
	implementation(libs.logback.classic)
}
