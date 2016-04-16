package com.hobo.bob.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadKey {
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
				if (key == null) {
					key = "bdea5de7e8da4e5f84fc1cdbbf6074ef";
				}
			}
		}
		
		return key;
	}
	
	private String read(String filename) {
		String key = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
			key = reader.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return key;
	}
}
