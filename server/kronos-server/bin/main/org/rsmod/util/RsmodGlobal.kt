package org.rsmod.util

import com.github.michaelbull.logging.InlineLogger
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.util.Modules
import org.rsmod.api.game.process.GameCycle
import org.rsmod.api.game.process.player.PlayerBuildAreaProcessor
import org.rsmod.api.player.forceDisconnect
import org.rsmod.api.registry.npc.NpcRegistry
import org.rsmod.api.registry.player.PlayerRegistry
import org.rsmod.api.repo.npc.NpcRepository
import org.rsmod.api.repo.player.PlayerRepository
import org.rsmod.api.utils.logging.GameExceptionHandler
import org.rsmod.game.entity.NpcList
import org.rsmod.game.entity.Player
import org.rsmod.game.entity.PlayerList
import org.rsmod.plugin.module.PluginModule
import org.rsmod.server.app.GameServerModule
import org.rsmod.server.shared.PluginConstants
import org.rsmod.server.shared.loader.PluginModuleLoader
import kotlin.time.measureTime

/**
 * @author Jire
 */
object RsmodGlobal {

	private val logger = InlineLogger()

	private val pluginPackages: Array<String>
		get() = PluginConstants.searchPackages

	@JvmStatic
	lateinit var injector: Injector

	@JvmStatic
	lateinit var gameCycle: GameCycle

	@JvmStatic
	lateinit var playerRepository: PlayerRepository

	@JvmStatic
	lateinit var npcRepository: NpcRepository

	@JvmStatic
	lateinit var playerRegistry: PlayerRegistry

	@JvmStatic
	lateinit var npcRegistry: NpcRegistry

	@JvmStatic
	lateinit var playerList: PlayerList

	@JvmStatic
	lateinit var npcList: NpcList

	@JvmStatic
	lateinit var buildAreas: PlayerBuildAreaProcessor

	@JvmStatic
	lateinit var exceptionHandler: GameExceptionHandler

	@JvmStatic
	fun init() {
		injector = createInjector()

		gameCycle = injector.getInstance(GameCycle::class.java)

		playerRepository = injector.getInstance(PlayerRepository::class.java)
		npcRepository = injector.getInstance(NpcRepository::class.java)

		playerRegistry = injector.getInstance(PlayerRegistry::class.java)
		npcRegistry = injector.getInstance(NpcRegistry::class.java)

		playerList = injector.getInstance(PlayerList::class.java)
		npcList = injector.getInstance(NpcList::class.java)

		buildAreas = injector.getInstance(PlayerBuildAreaProcessor::class.java)
		exceptionHandler = injector.getInstance(GameExceptionHandler::class.java)

		logger.info { "Initialized rsmod global!" }
	}

	@JvmStatic
	fun buildAreaTick() {
		playerList.forEach {
			it.tryOrDisconnect {
				buildAreas.process(this)
			}
		}
	}

	fun createInjector(): Injector {
		val pluginModules = loadModules()
		val modules = Modules.combine(GameServerModule, *pluginModules.toTypedArray())
		return Guice.createInjector(modules)
	}

	private fun loadModules(): Collection<AbstractModule> {
		logger.info { "Loading plugin modules..." }
		val modules: Collection<AbstractModule>
		val duration = measureTime {
			modules = PluginModuleLoader.load(PluginModule::class.java, pluginPackages)
		}
		reportDuration {
			"Loaded ${modules.size} plugin module${if (modules.size == 1) "" else "s"} in $duration."
		}
		return modules
	}

	private fun reportDuration(msg: () -> String) {
		logger.info { msg() }
	}

	private inline fun Player.tryOrDisconnect(block: Player.() -> Unit) =
		try {
			block(this)
		} catch (e: Exception) {
			forceDisconnect()
			exceptionHandler.handle(e) { "Error processing main cycle for player: $this." }
		} catch (e: NotImplementedError) {
			forceDisconnect()
			exceptionHandler.handle(e) { "Error processing main cycle for player: $this." }
		}

}
