plugins {
	id("reason-kotlin")
}

dependencies {
	api(projects.coreTaskApi)
	api(projects.logging)
	api(projects.properties)

	api(libs.google.gson)
	api(libs.hikaricp)
	api(libs.apache.commons.lang3)
	api(libs.google.http.client.gson)
	api(libs.mysql.connector.j)
	api(libs.okhttp)
	api(libs.fastutil)
	api(libs.classgraph)
	api(libs.netty.codec)
}
