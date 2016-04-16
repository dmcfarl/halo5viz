package com.hobo.bob.haloapi;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hobo.bob.HaloAPIConstants;
import com.hobo.bob.util.APICallCache;

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
			String call = "https://www.haloapi.com/metadata/" + HaloAPIConstants.TITLE + "/metadata/maps";
			result = (JSONArray)APICallCache.getInstance().getFromCache(call);
			if (result == null) {
				result = callArray(call);
				APICallCache.getInstance().cacheObject(call, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
