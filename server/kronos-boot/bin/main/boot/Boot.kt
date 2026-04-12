package boot

import io.ruin.Server
import io.ruin.rsprot.RSProtService
import logging.sentry.SentryLogging
import logging.Logging;

object Boot {

	@JvmStatic
	fun main(args: Array<String>) {
		Logging.configure()
		SentryLogging.initialize()
		try {
			Server.startCore()
			for (iModule in Server.iModules) {
				iModule.start()
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}
		RSProtService.start()
	}

}
