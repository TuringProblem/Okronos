plugins {
	id("reason-kotlin")
}

dependencies {
	implementation(libs.kotlinx.coroutines.core.jvm)

	api(projects.coreTask)
	api(projects.kronosApi)
	api(projects.kronosServerKotlin)
	api(projects.logging)
	api(projects.loggingSentry)
	api(projects.playerAttributes)
	api(projects.coreModuleApi)
	api(projects.coreTask)
	api(projects.coreTaskApi)
	api(projects.properties)
	api(projects.kronosServerDiscordServices)

	api(libs.rsprot.osrs.v231.api)

	api(libs.json)
	api(libs.google.guava)
	api(libs.google.guice)
	api(libs.jda)
	api(libs.jackson.module.kotlin)
	api(libs.jackson.dataformat.toml)
	api(libs.clikt)

	implementation(libs.alibaba.fastjson2)
	implementation(libs.jsoup)
	implementation(libs.jackson.dataformat.yaml)
	implementation(libs.apache.commons.codec)
	implementation(libs.unirest.java.core)
	implementation(libs.zero.allocation.hashing)
}
