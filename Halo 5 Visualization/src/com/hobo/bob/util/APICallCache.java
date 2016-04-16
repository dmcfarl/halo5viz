package com.hobo.bob.util;

import java.util.HashMap;
import java.util.Map;

public class APICallCache extends ReadFile {
	private Map<String, Object> cache = new HashMap<String, Object>();
	
	private static APICallCache instance = null;
	
	private APICallCache() {
	}
	
	public static APICallCache getInstance() {
		if (instance == null) {
			instance = new APICallCache();
		}
		
		return instance;
	}
	
	public void cacheObject(String call, Object response) {
		cache.put(call, response);
	}
	
	public Object getFromCache(String call) {
		return cache.get(call);
	}
}
