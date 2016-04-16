package com.hobo.bob.haloapi.filter;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hobo.bob.haloapi.MetadataCall;

public class MetadataFilter {
	public static String getMapName(String mapId) {
		JSONArray maps = MetadataCall.getMaps();
		String name = null;
		for (int i = 0; name == null && i < maps.length(); i++) {
			JSONObject map = maps.getJSONObject(i);
			if (mapId.equals(map.getString("id"))) {
				name = map.getString("name");
			}
		}
		
		return name;
	}
	
	public static String getMapId(String mapName) {
		JSONArray maps = MetadataCall.getMaps();
		String id = null;
		for (int i = 0; id == null && i < maps.length(); i++) {
			JSONObject map = maps.getJSONObject(i);
			if (mapName.trim().equals(map.getString("name").trim())) {
				id = map.getString("id");
			}
		}
		
		return id;
	}
}
