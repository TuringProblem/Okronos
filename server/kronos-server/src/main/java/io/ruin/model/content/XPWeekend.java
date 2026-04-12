package io.ruin.model.content;

import io.ruin.Server;
import io.ruin.model.World;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class XPWeekend {
	private static final long refresh_period = TimeUnit.HOURS.toMillis(1);

	public void start() {
		Server.worker.executor.scheduleAtFixedRate(this::load, refresh_period, refresh_period,
				TimeUnit.MILLISECONDS);

	}

	public void load() {
		if (isWeekend()) {
			World.weekendExpBoost = true;
			return;
		} else {
			if (!isWeekend()) {
				World.weekendExpBoost = false;
				return;
			}
		}
	}

	public static boolean isWeekend() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY
				|| Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
				|| Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
	}
}
