package com.hobo.bob.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hobo.bob.haloapi.filter.StatsFilter;

public class KillSet {

	private List<JSONObject> kills;
	private final int dimensions;

	public KillSet(int dimensions) {
		this(new ArrayList<JSONObject>(), dimensions);
	}

	public KillSet(List<JSONObject> kills, int dimensions) {
		this.dimensions = dimensions;
		this.kills = kills;
	}

	public List<JSONObject> getKills() {
		return kills;
	}

	public String toString() {
		return getOutput(true, true).toString();
	}

	public JSONArray getOutput(boolean includeKiller, boolean includeVictim) {
		JSONArray killsResult = new JSONArray();
		for (JSONObject kill : kills) {
			JSONArray killCoordinates = new JSONArray();
			if (includeKiller) {
				killCoordinates.put(getCoordinates(StatsFilter.getKillerLocation(kill, false)));
			}
			if (includeVictim) {
				killCoordinates.put(getCoordinates(StatsFilter.getVictimLocation(kill, false)));
			}
			killsResult.put(killCoordinates);
		}

		return killsResult;
	}

	public boolean isMultiKill() {
		return kills.size() > 1;
	}

	public int getMultiKillSize() {
		return kills.size();
	}

	public String getKiller() {
		// Assume killer is the same for all kills in this set.
		String killer = null;
		if (!kills.isEmpty()) {
			killer = StatsFilter.getKillerGamertag(kills.get(0));
		}
		return killer;
	}

	private JSONArray getCoordinates(JSONObject location) {
		JSONArray coordinates = new JSONArray();
		assert (dimensions == 2 || dimensions == 3);
		if (dimensions >= 2) {
			coordinates.put(location.getDouble("x"));
			coordinates.put(location.getDouble("y"));
		}
		if (dimensions == 3) {
			coordinates.put(location.getDouble("z"));
		}

		return coordinates;
	}
}
