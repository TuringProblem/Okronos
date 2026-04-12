package io.ruin.kotlin.content.event

import com.google.common.base.Stopwatch
import java.util.concurrent.TimeUnit
import kotlin.concurrent.fixedRateTimer

/**
 *
 * @project Kronos
 * @author ReverendDread on 5/12/2020
 * https://www.rune-server.ee/members/reverenddread/
 */
object TimedEventManager {

	private val stopwatch = Stopwatch.createUnstarted();
	private const val eventDurationMinutes = 30

	var event: TimedEventImpl? = null
		set(value) {
			field?.onEventStopped()
			value?.onEventStart()
			value?.let {
				stopwatch.reset()
				stopwatch.start()
			}
			field = value
		}

	fun tick() {
		event?.let {
			if (stopwatch.elapsed(TimeUnit.MINUTES) >= eventDurationMinutes) {
				event = null
			}
			event?.tick()
		}
	}

	init {
		val delay = TimeUnit.HOURS.toMillis(1);
		fixedRateTimer("Event-Boss-Timer", false, delay, delay) {
			//  var boss = Random.get(EventBossType.values())
			//boss?.let { event = EventBoss(boss) }
		}
	}

}
