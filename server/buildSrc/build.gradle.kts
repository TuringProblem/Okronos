plugins {
	`kotlin-dsl`
}

repositories {
	mavenCentral()
	gradlePluginPortal()
}

dependencies {
	implementation(kotlin("gradle-plugin"))
	implementation(libs.zero.allocation.hashing)
	implementation("net.ltgt.gradle:gradle-errorprone-plugin:4.3.0")
	implementation("org.zeroturnaround.gradle.jrebel:org.zeroturnaround.gradle.jrebel.gradle.plugin:1.2.1")
	implementation("org.ow2.asm:asm:9.7.1")
	implementation("net.openhft:zero-allocation-hashing:0.16")
}
