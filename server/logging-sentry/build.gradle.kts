plugins {
	id("reason-kotlin")
}

dependencies {
	api(libs.sentry)
	implementation(libs.sentry.logback)

	implementation(projects.properties)
	implementation(projects.logging)
}
