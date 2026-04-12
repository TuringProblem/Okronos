package io.ruin.model.content;

import java.util.Calendar;

public class PestControl {

	public static boolean isDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY;
	}
}
