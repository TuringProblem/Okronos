rootProject.name = "reason-server"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
	@Suppress("UnstableApiUsage")
	repositories {
		mavenCentral()
		gradlePluginPortal()
	}
}

pluginManagement {
	val kotlinVersion: String = settings.extra["com.reasonps.gradle.kotlin.version"] as String
	plugins {
		kotlin("jvm").version(kotlinVersion)
	}

	repositories {
		mavenCentral()
		gradlePluginPortal()
	}
}

include(
	"core-task",
	"core-task-api",
	"core-combat-api-reason",
	"core-module-api",
	"core-module-test",
	"core-database",
	"player-attributes",
	"player-attributes-api",
	"player-mongo",
)

include(
	"kronos-api",
	"kronos-server",
	"kronos-server-kotlin",
	"kronos-server-kotlin-coupled",
	"kronos-webhooks",
	"kronos-boot",
	"logging",
	"logging-sentry",
	"properties",
	"world-list",
)

include(
	"interface-advancedsettings",
	"interface-tabsettings",
	"interface-ikod",
	"interface-charactercreator",
	"player-chat-filter",
	"collectionlog",
	"friendlist",
	"clanchat",
	"player-music",
	"player-groupiron",
	"tradepost",
	"royaltitans",
	"npc-nex",
	"dominion-of-echoes",
	"doom-of-mokhaiotl",
	"yama",
	"donationdeals",
	"tormenteddemon",
	"gemstone-crab",
	"kronos-server-discord-services"
)


// run tests only when explicitly requested
gradle.beforeProject {
	val isTestExplicitlyRequested = gradle.startParameter.taskNames.any { it.contains("test", ignoreCase = true) }
	tasks.matching { it.name == "test" }.configureEach {
			enabled = isTestExplicitlyRequested
	}
}