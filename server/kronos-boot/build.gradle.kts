import org.gradle.internal.os.OperatingSystem

plugins {
	id("reason-kotlin")
	application
}

dependencies {
	implementation(projects.kronosServer)
	implementation(projects.kronosServerKotlinCoupled)

	implementation(projects.logging)
	implementation(projects.loggingSentry)

	implementation(projects.playerAttributes)
	implementation(projects.coreCombatApiReason)

	implementation(projects.interfaceAdvancedsettings)
	implementation(projects.interfaceTabsettings)
	implementation(projects.interfaceIkod)
	implementation(projects.interfaceCharactercreator)
	implementation(projects.coreModuleTest)
	implementation(projects.collectionlog)
	implementation(projects.friendlist)
	implementation(projects.clanchat)
	implementation(projects.playerMusic)
	implementation(projects.playerGroupiron)
	implementation(projects.playerChatFilter)
	implementation(projects.royaltitans)
	implementation(projects.npcNex)
	implementation(projects.dominionOfEchoes)
	implementation(projects.doomOfMokhaiotl)
	implementation(projects.yama)
	implementation(projects.donationdeals)
	implementation(projects.tormenteddemon)
	implementation(projects.gemstoneCrab)
	implementation(projects.worldList)
	implementation(projects.tradepost)
	implementation(projects.playerMongo)

	implementation(libs.hotswap.agent.core)
}

val defaultJvmArgs = listOf(
	"-XX:-OmitStackTraceInFastThrow",
	"-Xms1g",
	"-XX:AutoBoxCacheMax=65535",
	"--enable-preview",
	"--add-opens=java.base/jdk.internal.vm=ALL-UNNAMED",
	"--add-opens=java.base/jdk.internal.loader=ALL-UNNAMED",
	"--add-opens=java.base/java.lang=ALL-UNNAMED",
	"--add-opens=java.base/java.util=ALL-UNNAMED",
	"--enable-native-access=ALL-UNNAMED",
	"-XX:CompileThreshold=1500",
	"-Dslf4j.internal.verbosity=warn",
	"-XX:+UseCodeCacheFlushing",
	"-XX:ReservedCodeCacheSize=128M",
)

application {
	mainClass.set("boot.Boot")
	applicationDefaultJvmArgs += defaultJvmArgs
}

val buildCache = tasks.register<Exec>("build_cache") {
	group = "_reason"

	workingDir = rootDir
	val os = OperatingSystem.current()
	if (os.isWindows) {
		commandLine(".dev/tool-cache-packer.exe")
	} else {
		commandLine(".dev/tool-cache-packer")
	}
	environment("RUST_LOG", "info")
	args("--in", "./data/cache/toml/", "--out", "./data/cache/")
}

pluginManager.withPlugin("application") {
	tasks.run.configure {
		workingDir = rootProject.projectDir
	}

	tasks.installDist {
		dependsOn(buildCache)
	}
}

val jrebelRoot = rootDir.resolve(".jrebel/lib/")
val jrebelAgentPath: String = when {
	OperatingSystem.current().isWindows -> jrebelRoot.resolve("jrebel64.dll").absolutePath
	OperatingSystem.current().isMacOsX -> jrebelRoot.resolve("libjrebel64.dylib").absolutePath
	OperatingSystem.current().isLinux -> jrebelRoot.resolve("libjrebel64.so").absolutePath
	else -> throw GradleException("Unsupported OS for JRebel agent")
}

tasks.register<JavaExec>("run_hotswap") {
	environment["RUST_LOG"] = "info"
	classpath = sourceSets["main"].runtimeClasspath
	workingDir = rootDir

	jvmArgs = defaultJvmArgs + listOf(
		"-agentpath:$jrebelAgentPath",
		"-Xlog:redefine+class*=warning",
	)
	mainClass.set("boot.Boot")
	dependsOn("hotswap_copy")
}
