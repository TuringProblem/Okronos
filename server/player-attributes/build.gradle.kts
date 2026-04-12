plugins {
	id("reason-kotlin")
}

dependencies {
	implementation(projects.logging)
	implementation(projects.properties)
	implementation(libs.google.gson)
	implementation(libs.alibaba.fastjson2)
}
