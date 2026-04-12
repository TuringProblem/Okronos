package io.ruin.api.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;

@Slf4j
public class JsonUtils {

	/**
	 * misc
	 */
	public static final Gson GSON = new GsonBuilder().create();

	/**
	 * used by dumps
	 */
	public static final Gson GSON_PRETTY = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * friends serializer
	 */
	public static final Gson GSON_EXPOSE = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

	public static String toJson(Object object) {
		return GSON.toJson(object);
	}

	public static String toPrettyJson(Object object) {
		return GSON_PRETTY.toJson(object);
	}

	public static <T> T fromJson(String json, Type rawType, Type... typeArguments) {
		return GSON.fromJson(json, TypeToken.getParameterized(rawType, typeArguments).getType());
	}

	public static void toFile(File file, String json) throws IOException {
		try (BufferedWriter bw = Files.newBufferedWriter(file.toPath())) {
			bw.write(json);
		}
	}

	public static String fromFile(File jsonFile) throws IOException {
		try (FileReader reader = new FileReader(jsonFile)) {
			JsonElement element = JsonParser.parseReader(reader);
			return GSON.toJson(element);
		} catch (Throwable t) {
			ServerWrapper.logError("JSON Failure: " + jsonFile.getAbsolutePath(), t);
			return null;
		}
	}

}
