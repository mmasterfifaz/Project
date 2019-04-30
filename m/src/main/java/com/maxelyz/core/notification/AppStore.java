/*
 * Comet4J Copyright(c) 2011, http://code.google.com/p/comet4j/ This code is
 * licensed under BSD license. Use it as you wish, but keep this copyright
 * intact.
 */
package com.maxelyz.core.notification;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 应用全局存储
 * @author jinghai.xiao@gmail.com
 * @date 2011-2-25
 */

public class AppStore {

	private static Map<String, String> map;
	private static AppStore instance;

	public static AppStore getInstance() {

		if (instance == null) {
			instance = new AppStore();
			map = new ConcurrentSkipListMap<String, String>();
		}
		return instance;
	}

	public void put(String key, String value) {
		map.put(key, value);
	}

	public String get(String key) {
		return map.get(key);
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void destroy() {
		map.clear();
		map = null;
	}

}
