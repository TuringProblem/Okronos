package io.ruin.api.utils;

import com.google.common.base.Preconditions;

import java.util.EnumMap;
import java.util.Map;

public class RsAttributesHolder {

	protected transient Map<RsAttribute, Object> properties = null;

	protected Map<RsAttribute, Object> properties() {
		return properties;
	}

	@SuppressWarnings("UnusedReturnValue")
	protected void set(RsAttribute key, Object value) {
		Preconditions.checkNotNull(key);
		if (properties == null)
			properties = new EnumMap<>(RsAttribute.class);
		properties.put(key, value);
	}

	@SuppressWarnings("unchecked")
	protected <T> T get(RsAttribute key) {
		if (properties == null)
			return null;
		return (T) properties.get(key);
	}

	@SuppressWarnings("unchecked")
	protected <T> T get(RsAttribute key, T defaultValue) {
		if (properties == null)
			return defaultValue;
		return (T) properties.getOrDefault(key, defaultValue);
	}

	@SuppressWarnings("unchecked")
	public <T> T remove(RsAttribute key) {
		if (properties == null)
			return null;
		Object value = properties.remove(key);
		return value == null ? null : (T) value;
	}
}
