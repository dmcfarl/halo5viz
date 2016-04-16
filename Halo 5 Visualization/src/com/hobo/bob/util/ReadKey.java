package com.hobo.bob.util;

public class ReadKey extends ReadFile {
	private static String key = null;
	
	private static ReadKey instance = null;
	
	private ReadKey() {
	}
	
	public static ReadKey getInstance() {
		if (instance == null) {
			instance = new ReadKey();
		}
		
		return instance;
	}
	
	public String getKey() {
		synchronized (this) {
			if (key == null) {
				key = read("key.txt");
				if (key != null) {
					key = key.trim();
				}
			}
		}
		
		return key;
	}
}
