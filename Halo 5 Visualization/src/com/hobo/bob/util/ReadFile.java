package com.hobo.bob.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {
	public static String read(String filename) {
		StringBuffer data = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line = null;
			while ((line = reader.readLine()) != null) {
				data.append(line);
				data.append("\n");
			}
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
		
		return data.toString();
	}
}
