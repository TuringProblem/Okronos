package io.ruin.api.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

public class SimpleLogger {

	private final String path;

	private final boolean print;

	public SimpleLogger(String path, boolean print) {
		this.path = path;
		this.print = print;
	}

	public void log(String message) {
		log(message, false);
	}

	public void log(String message, boolean timestamp) {
		if (timestamp)
			message = TimeUtils.addTimestamp(message);
		if (print)
			System.out.println(message);
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
			bw.write(message);
			bw.newLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void log(Throwable t) {
		if (print)
			t.printStackTrace();
		try (PrintWriter out = new PrintWriter(new FileWriter(path, true))) {
			t.printStackTrace(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}