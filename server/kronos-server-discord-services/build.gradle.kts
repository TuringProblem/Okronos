plugins {
	id("java")
	id("eclipse")
}

repositories {
	mavenCentral()
}

dependencies {
	val lombokVersion: String =
		providers
			.gradleProperty("com.reasonps.gradle.lombok.version")
			.get()
	compileOnly("org.projectlombok:lombok:$lombokVersion")
	annotationProcessor("org.projectlombok:lombok:$lombokVersion")

	implementation(projects.logging)
	implementation(projects.coreModuleApi)
	implementation(projects.kronosApi)
	implementation(projects.properties)

	implementation(libs.jackson.dataformat.toml)
	implementation(libs.unirest.java.core)
	implementation(libs.json)
}

tasks.test {
	useJUnitPlatform()
}