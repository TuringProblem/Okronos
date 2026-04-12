package com.reasonps.worldlist

import core.module.api.IModule
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ruin.central.utility.WorldList
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Jire
 */
class WorldListServer : IModule {

	override fun start() {
		start(9292)
	}

	private fun start(port: Int): EmbeddedServer<*, *> {
		log.info("Starting world list server on port {}...", port)
		val server = embeddedServer(
			Netty,
			port = port,
			module = {
				routing {
					get("/") {
						call.respondText { "World list" }
					}
					get("/worlds.ws") {
						call.respondBytes(WorldList.worldListData)
					}
				}
			}
		).start(wait = false)
		return server
	}

	private companion object {
		private val log: Logger = LoggerFactory.getLogger(WorldListServer::class.java)
	}

}
