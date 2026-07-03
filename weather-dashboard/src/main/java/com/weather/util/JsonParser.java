package com.weather.util;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON parsing utilities for weather data.
 */
public class JsonParser {
    public static double getDouble(JSONObject obj, String key, double defaultValue) {
        try {
            return obj.getDouble(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static int getInt(JSONObject obj, String key, int defaultValue) {
        try {
            return obj.getInt(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String getString(JSONObject obj, String key, String defaultValue) {
        try {
            return obj.getString(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static boolean getBoolean(JSONObject obj, String key, boolean defaultValue) {
        try {
            return obj.getBoolean(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static List<JSONObject> getJsonArray(JSONObject obj, String key) {
        List<JSONObject> list = new ArrayList<>();
        try {
            JSONArray arr = obj.getJSONArray(key);
            for (int i = 0; i < arr.length(); i++) {
                list.add(arr.getJSONObject(i));
            }
        } catch (Exception e) {
            // ignore
        }
        return list;
    }
}
