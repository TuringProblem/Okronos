plugins {
	id("reason-kotlin")
}

dependencies {
	api("io.prometheus:prometheus-metrics-core:1.4.1")
	implementation(projects.logging)
	implementation(projects.properties)
	implementation("io.prometheus:prometheus-metrics-instrumentation-jvm:1.4.1")
	implementation("io.prometheus:prometheus-metrics-exporter-httpserver:1.4.1")
}
