plugins {
	id("reason-kotlin")
}

dependencies {
	api(libs.netty.buffer)
	api(libs.kotlin.inline.logger.jvm)
}
