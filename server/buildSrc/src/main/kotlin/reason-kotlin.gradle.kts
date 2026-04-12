import java.security.MessageDigest;
import net.ltgt.gradle.errorprone.errorprone
import org.zeroturnaround.jrebel.gradle.dsl.RebelDslClasspath
import org.zeroturnaround.jrebel.gradle.dsl.RebelDslClasspathResource
import org.objectweb.asm.*
import net.openhft.hashing.LongHashFunction

plugins {
	kotlin("jvm")
	eclipse
	id("net.ltgt.errorprone")
	id("org.zeroturnaround.gradle.jrebel")
}

dependencies {
	val lombokVersion: String =	providers.gradleProperty("com.reasonps.gradle.lombok.version").get()
	compileOnly("org.projectlombok:lombok:$lombokVersion")
	annotationProcessor("org.projectlombok:lombok:$lombokVersion")
	testCompileOnly("org.projectlombok:lombok:$lombokVersion")
	testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")

	testImplementation("org.junit.jupiter:junit-jupiter:5.13.4")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	errorprone("com.google.errorprone:error_prone_core:2.41.0")
}

kotlin {
	jvmToolchain(24)

	compilerOptions {
		freeCompilerArgs = listOf("-Xcontext-receivers")
		optIn = listOf("kotlin.contracts.ExperimentalContracts")
	}
}

rebel {
	classpath = RebelDslClasspath()
	val hotswapResource = RebelDslClasspathResource();
	hotswapResource.directory = "build/classes/hotswap/"
	classpath.addResource(hotswapResource)
	classpath.omitDefaultClassesDir = true
	classpath.omitDefaultResourcesDir = true
}

tasks.register<Copy>("hotswap_copy") {
	val inputDirectory = file("build/classes/java/main/")
	val outputDirectory = file("build/classes/hotswap/")

	into("build/classes/hotswap/")
	from("build/classes/java/main/")

	eachFile {
		val targetAbsolute = relativePath.getFile(outputDirectory)
		val sourceAbsolute = relativePath.getFile(inputDirectory)

		val targetSha = ClassHash.generate(targetAbsolute)
		val sourceSha = ClassHash.generate(sourceAbsolute)

		if (targetSha == null || sourceSha == null) {
			return@eachFile
		}

		if (targetSha == sourceSha) {
			exclude()
		} else {
			println("hotswap_copy: $targetAbsolute")
		}
	}
	includeEmptyDirs = false
}

tasks.named("classes") {
	finalizedBy("hotswap_copy")
}

object ClassHash {
	fun generate(file: File): String? {
		return try {
			val normalizedBytes = normalizedClassBytes(file)
			val hashLong = LongHashFunction.xx().hashBytes(normalizedBytes);
			BigInteger.valueOf(hashLong).toString(16).padStart(32, '0')
		} catch (e: Exception) {
			null
		}
	}

	// sometimes classes have timestamp information or debug information
	// that is changed each 'touch', recompile, even though the code itself
	// is not.
	fun normalizedClassBytes(file: File): ByteArray {
			val cr = ClassReader(file.readBytes())
			val cw = ClassWriter(0)
			cr.accept(cw, ClassReader.SKIP_DEBUG or ClassReader.SKIP_FRAMES)
			return cw.toByteArray()
	}
}

tasks.test {
	useJUnitPlatform()
	jvmArgs = listOf(
		"-XX:-OmitStackTraceInFastThrow",
		"-XX:AutoBoxCacheMax=65535",
		"--enable-preview",
		"--add-opens=java.base/jdk.internal.vm=ALL-UNNAMED",
		"--add-opens=java.base/java.util=ALL-UNNAMED",
		"--add-opens=java.base/java.lang=ALL-UNNAMED",
		"-XX:+UseCodeCacheFlushing",
		"-XX:ReservedCodeCacheSize=128M",
		"-Dslf4j.internal.verbosity=error",
		"-Dorg.slf4j.simpleLogger.defaultLogLevel=error"
	)

	testLogging {
		showStandardStreams = true
		events("passed", "failed", "skipped", "standardOut", "standardError")
		exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
	}

	workingDir = rootDir
}

if (!project.hasProperty("enableErrorProne")) {
	tasks.withType<JavaCompile>().configureEach {
		options.errorprone.isEnabled.set(false)
	}
}

eclipse.jdt.file.withProperties {
	this["org.eclipse.jdt.core.compiler.problem.enablePreviewFeatures"] = "enabled"
	this["org.eclipse.jdt.core.compiler.problem.reportPreviewFeatures"] = "ignore"
	this["org.eclipse.jdt.core.compiler.problem.reportPreviewFeatures"] = "ignore"
	this["org.eclipse.jdt.core.codeComplete.subwordMatch"] = "enabled"
}

tasks.withType<JavaCompile>().configureEach {
	options.compilerArgs.addAll(
		listOf(
			"--enable-preview",
			"--add-exports=java.base/jdk.internal.vm=ALL-UNNAMED",
		)
	)

	options.errorprone {
		disable("ParameterName")
		disable("UnusedVariable")
		disable("StringCaseLocaleUsage")
		disable("IdentityBinaryExpression")
		disable("NarrowingCompoundAssignment")
		disable("StringCharset")
		disable("InconsistentCapitalization")
		disable("AssignmentExpression")
		disable("JdkObsolete")
	}
}
