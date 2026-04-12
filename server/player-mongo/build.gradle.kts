plugins {
	id("reason-kotlin")
}

dependencies {
	implementation(projects.kronosServer)
	implementation("org.mongodb:mongodb-driver-reactivestreams:5.3.0")
	implementation("io.projectreactor:reactor-core:3.8.0-M6")
	implementation(project(":properties"))
}
