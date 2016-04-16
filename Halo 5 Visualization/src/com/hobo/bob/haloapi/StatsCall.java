package com.hobo.bob.haloapi;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hobo.bob.HaloAPIConstants;
import com.hobo.bob.haloapi.filter.StatsFilter;
import com.hobo.bob.util.APICallCache;

public class StatsCall extends APICall {
	public static JSONObject getLastMatch(String player, String mode, String start, String count) {
		JSONObject result = null;
		try {
			JSONObject response = callObject("https://www.haloapi.com/stats/" + HaloAPIConstants.TITLE + "/players/"
					+ URLEncoder.encode(player, "UTF-8") + "/matches?modes=" + mode + "&start=" + start + "&count="
					+ count);
			result = response.getJSONArray("Results").getJSONObject(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static JSONObject getMatchEvents(String matchId) {
		JSONObject result = null;
		try {
			String call = "https://www.haloapi.com/stats/" + HaloAPIConstants.TITLE + "/matches/" + matchId + "/events";
			result = (JSONObject) APICallCache.getInstance().getFromCache(call);
			if (result == null) {
				result = callObject(call);
				APICallCache.getInstance().cacheObject(call, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static List<JSONObject> getMatchIDs(String mode, String mapId, int length, String... players) {
		Map<String, JSONObject> result = new HashMap<String, JSONObject>();
		for (String player : players) {
			List<JSONObject> playerResult = new ArrayList<JSONObject>();
			try {
				int retries = 0;
				int count = 25;
				while (playerResult.size() < length && retries < 50) {
					JSONObject response = callObject("https://www.haloapi.com/stats/" + HaloAPIConstants.TITLE
							+ "/players/" + URLEncoder.encode(player, "UTF-8") + "/matches?modes=" + mode + "&start="
							+ (retries * count + 1) + "&count=" + count);
					JSONArray resultsArray = response.getJSONArray("Results");
					for (int i = 0; i < resultsArray.length() && playerResult.size() < length; i++) {
						JSONObject match = resultsArray.getJSONObject(i);
						if (StatsFilter.getMapId(match).equals(mapId)) {
							System.out.println("Found mapId!");
							playerResult.add(match);
						}
					}
					retries++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			for (JSONObject match : playerResult) {
				result.put(StatsFilter.getMatchId(match), match);
			}
		}

		return new ArrayList<JSONObject>(result.values());
	}

	public static List<JSONObject> getMatchEvents(String mode, String mapId, int numMatchesPerPlayer, String... players) {
		Map<String, JSONObject> result = new HashMap<String, JSONObject>();
		for (String player : players) {
			Map<String, JSONObject> playerResult = new HashMap<String, JSONObject>();

			try {
				int retries = 0;
				int toRetrieve = numMatchesPerPlayer * 5 > 25 ? 25 : numMatchesPerPlayer * 5;
				int retryMax = numMatchesPerPlayer * 10;
				int retrieved = toRetrieve;

				while (playerResult.size() < numMatchesPerPlayer && retries < retryMax && retrieved > 0) {
					try {
						JSONObject response = callObject("https://www.haloapi.com/stats/" + HaloAPIConstants.TITLE
								+ "/players/" + URLEncoder.encode(player, "UTF-8") + "/matches?modes=" + mode
								+ "&start=" + (retries * toRetrieve + 1) + "&count=" + toRetrieve);
						JSONArray resultsArray = response.getJSONArray("Results");
						for (int i = 0; i < resultsArray.length() && playerResult.size() < numMatchesPerPlayer; i++) {
							try {
								JSONObject match = resultsArray.getJSONObject(i);
								if (StatsFilter.getMapId(match).equals(mapId)) {
									System.out.println("Found mapId!");
									String matchId = StatsFilter.getMatchId(match);
									JSONObject events = getMatchEvents(matchId);
									if (events != null) {
										playerResult.put(matchId, events);
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						retrieved = response.getInt("Count");
					} catch (Exception e) {
						e.printStackTrace();
					}
					retries++;
				}

				result.putAll(playerResult);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return new ArrayList<JSONObject>(result.values());
	}

}
