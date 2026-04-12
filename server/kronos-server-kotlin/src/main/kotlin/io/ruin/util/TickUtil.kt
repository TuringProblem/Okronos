package io.ruin.util

import java.time.Duration

fun Long.ticksPassed(ticks: Int): Boolean = (System.currentTimeMillis() - this) / 600 >= ticks

class TimeHolder(val key: String? = null) {

	constructor() : this(null) {
		lastTime = System.currentTimeMillis()
	}

	var lastTime: Long = 0

	fun bump() {
		lastTime = System.currentTimeMillis()
	}

	fun passed(ticks: Int, function: () -> Unit): Boolean {
		var passed = System.currentTimeMillis() - lastTime / 600 >= ticks
		if (passed) {
			bump()
			function.invoke()
		}
		return passed
	}

	fun passed(d: Duration): Boolean = System.currentTimeMillis() - lastTime >= d.toMillis()

	var duration = TickDuration(Duration.ofMillis(System.currentTimeMillis() - lastTime))
	var lifetime = duration

	var ticksSince = duration.ticks

}

class TickDuration(val duration: Duration) {
	var ticks: Long = duration.toMillis() / 600L
}