package com.marekturis.common.domain.event;


import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Marek Turis
 */
public class JsonParsableEvent implements ParsableEvent {

	private Map<String, Object> fields = new HashMap<String, Object>();

	public JsonParsableEvent(String json) {
		JSONObject jsonObject = new JSONObject(json);
		initializeLowerCaseFields(jsonObject.toMap());
	}

	private void initializeLowerCaseFields(Map<String, Object> stringObjectMap) {
		for (Map.Entry<String, Object> entry : stringObjectMap.entrySet()) {
			fields.put(normalizeFieldKey(entry.getKey()), entry.getValue());
		}
	}

	private String normalizeFieldKey(String oldKey) {
		return oldKey.toLowerCase().replace("_", "");
	}


	public String getString(String key) {
		return getValue(key, String.class);
	}

	public int getInt(String key) {
		return getValue(key, Integer.class);
	}

	public long getLong(String key) {
		return getValue(key, Long.class);
	}

	public double getDouble(String key) {
		return getValue(key, Double.class);
	}

	public Date getDate(String key) {
		long timestamp = getLong(key);
		return new Date(timestamp);
	}

	@Override
	public boolean getBoolean(String key) {
		return getValue(key, Boolean.class);
	}

	private <T> T getValue(String key, Class<T> valueType) {
		String normalizedKey = normalizeFieldKey(key);
		return valueType.cast(fields.get(normalizedKey));
	}

	@Override
	public String identity() {
		return getString("identity");
	}

	public Date occuredOn() {
		return getDate("occuredOn");
	}

	public String name() {
		return getString("name");
	}
}
