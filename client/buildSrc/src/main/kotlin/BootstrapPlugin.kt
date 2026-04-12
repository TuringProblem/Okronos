import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*

class BootstrapPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        val bootstrapDependencies by configurations.creating {
            isCanBeConsumed = false
            isCanBeResolved = true
            isTransitive = false
        }

        dependencies {
            bootstrapDependencies(project(":runelite-api"))
            bootstrapDependencies(project(":client"))
            bootstrapDependencies(project(":jshell"))
            bootstrapDependencies(group = "", name = "runelite-patch")
        }

        tasks.register<BootstrapTask>("bootstrapStaging", "staging")
        tasks.register<BootstrapTask>("bootstrapNightly", "nightly")
        tasks.register<BootstrapTask>("bootstrapSnapshot", "snapshot")
        tasks.register<BootstrapTask>("bootstrapStable", "stable")
        tasks.register<BootstrapTask>("bootstrapExperimental", "experimental")

        tasks.withType<BootstrapTask> {
            this.group = "runelite-parent"
            this.clientJar.fileProvider(provider { tasks["jar"].outputs.files.singleFile })

            dependsOn(bootstrapDependencies)

            doLast {
                val bootstrapDir = layout.buildDirectory.dir("bootstrap/${type}").get().asFile
                println("Copying dependencies to ${bootstrapDir.path}")
                copy {
                    from(bootstrapDependencies)
                    into(bootstrapDir)
                }
            }
        }
    }
}
