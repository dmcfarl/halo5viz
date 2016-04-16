package com.hobo.bob.haloapi;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hobo.bob.HaloAPIConstants;

public class MetadataCall extends APICall {
	public static JSONObject getMapVariants(String mapId) {
		JSONObject result = null;
		try {
			result = callObject(
					"https://www.haloapi.com/metadata/" + HaloAPIConstants.TITLE + "/metadata/map-variants/" + mapId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public static JSONArray getMaps() {
		JSONArray result = null;
		try {
			result = callArray(
					"https://www.haloapi.com/metadata/" + HaloAPIConstants.TITLE + "/metadata/maps");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
