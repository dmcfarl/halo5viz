package com.hobo.bob.haloapi.filter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hobo.bob.HaloAPIConstants;
import com.hobo.bob.model.KillSet;

public class StatsFilter {
	public static String getMatchId(JSONObject match) {
		JSONObject id = match.getJSONObject("Id");
		return id.getString("MatchId");
	}

	public static List<String> getMatchIds(JSONArray matches) {
		List<String> ids = new ArrayList<String>();
		for (int i = 0; i < matches.length(); i++) {
			JSONObject match = matches.getJSONObject(i);
			JSONObject id = match.getJSONObject("Id");
			String matchId = id.getString("MatchId");
			System.out.println("MatchId:" + matchId);
			ids.add(matchId);
		}

		return ids;
	}

	public static String getMapId(JSONObject match) {
		return match.getString("MapId");
	}

	public static String getKillerGamertag(JSONObject event) {
		String gamertag = null;
		if (!event.isNull("Killer")) {
			gamertag = event.getJSONObject("Killer").getString("Gamertag");
		}
		return gamertag;
	}

	public static String getVictimGamertag(JSONObject event) {
		String gamertag = null;
		if (!event.isNull("Victim")) {
			gamertag = event.getJSONObject("Victim").getString("Gamertag");
		}
		return gamertag;
	}

	public static JSONObject getKillerLocation(JSONObject event, boolean includeNullGamertag) {
		JSONObject location = null;
		if ((includeNullGamertag || !event.isNull("Killer")) && !event.isNull("KillerWorldLocation")) {
			location = event.getJSONObject("KillerWorldLocation");
		}

		return location;
	}

	public static JSONObject getVictimLocation(JSONObject event, boolean includeNullGamertag) {
		JSONObject location = null;
		if ((includeNullGamertag || !event.isNull("Victim")) && !event.isNull("VictimWorldLocation")) {
			location = event.getJSONObject("VictimWorldLocation");
		}

		return location;
	}

	public static boolean isEnemyKill(JSONObject event) {
		return event.getInt("DeathDisposition") == 1;
	}

	private static List<JSONObject> filterPlayerEvents(String[] players, boolean includeKills, boolean includeDeaths,
			JSONObject... events) {
		List<JSONObject> playerEvents = new ArrayList<JSONObject>();

		for (JSONObject eventObj : events) {
			JSONArray gameEvents = eventObj.getJSONArray("GameEvents");
			for (int i = 0; i < gameEvents.length(); i++) {
				JSONObject event = gameEvents.getJSONObject(i);
				try {
					if (includeKills && !event.isNull("Killer")) {
						for (String player : players) {
							if (event.getJSONObject("Killer").getString("Gamertag").equals(player)) {
								playerEvents.add(event);
							}
						}
					} else if (includeDeaths && !event.isNull("Victim")) {
						for (String player : players) {
							if (event.getJSONObject("Victim").getString("Gamertag").equals(player)) {
								playerEvents.add(event);
							}
						}
					}
				} catch (Exception e) {
					System.out.println(event.toString(4));
					throw e;
				}
			}
		}

		return playerEvents;
	}

	public static List<JSONObject> filterPlayerKills(List<JSONObject> events, String... players) {
		return filterPlayerEvents(players, true, false, events.toArray(new JSONObject[1]));
	}

	public static List<JSONObject> filterPlayerDeaths(List<JSONObject> events, String... players) {
		return filterPlayerEvents(players, false, true, events.toArray(new JSONObject[1]));
	}

	public static List<JSONObject> filterPlayerEvents(List<JSONObject> events, String... players) {
		return filterPlayerEvents(players, true, true, events.toArray(new JSONObject[1]));
	}

	private static List<String> filterAll(List<JSONObject> playerEvents, String[] returnFormat, String coordinateFormat,
			int dimensions, boolean includeKillLocation, boolean includeVictimLocation, boolean includeDeathLocation,
			boolean includeKillerLocation, String... players) {
		int numLocations = (includeKillLocation ? 1 : 0) + (includeVictimLocation ? 1 : 0)
				+ (includeDeathLocation ? 1 : 0) + (includeKillerLocation ? 1 : 0);
		if (returnFormat.length != numLocations) {
			throw new IllegalArgumentException("Number of return formats did not match number of flags: formats: "
					+ returnFormat.length + " locations: " + numLocations);
		}
		StringBuffer[] coordinates = new StringBuffer[4];
		for (int i = 0; i < coordinates.length; i++) {
			coordinates[i] = new StringBuffer();
		}

		List<String> playerList = Arrays.asList(players);
		for (int i = 0; i < playerEvents.size(); i++) {
			JSONObject event = playerEvents.get(i);

			boolean kill = playerList.contains(getKillerGamertag(event));

			String[] killCoordinates = null;
			if (((kill && includeKillLocation) || (!kill && includeKillerLocation))) {
				JSONObject location = getKillerLocation(event, false);
				if (location != null) {
					killCoordinates = getCoordinates(location, dimensions);
				}
			}
			String[] victimCoordinates = null;
			if (((kill && includeVictimLocation) || (!kill && includeDeathLocation))) {
				JSONObject location = getVictimLocation(event, true);
				if (location != null) {
					victimCoordinates = getCoordinates(location, dimensions);
				}
			}

			if (killCoordinates != null) {
				coordinates[kill ? 0 : 2].append(String.format(coordinateFormat, (Object[]) killCoordinates));
				coordinates[kill ? 0 : 2].append(",\n");
			}

			if (victimCoordinates != null) {
				coordinates[kill ? 1 : 3].append(String.format(coordinateFormat, (Object[]) victimCoordinates));
				coordinates[kill ? 1 : 3].append(",\n");
			}
		}

		List<String> coordinateList = new ArrayList<String>();
		int formatCount = 0;
		for (int i = 0; i < coordinates.length; i++) {
			if (coordinates[i].length() > 2) {
				coordinates[i].delete(coordinates[i].length() - 2, coordinates[i].length());
				coordinateList.add(String.format(returnFormat[formatCount], coordinates[i].toString()));
				formatCount++;
			}
		}

		return coordinateList;
	}

	public static List<String> filterAll(List<JSONObject> playerEvents, String[] returnFormat, String coordinateFormat,
			int dimensions, String... players) {
		return filterAll(playerEvents, returnFormat, coordinateFormat, dimensions, true, true, true, true, players);
	}

	public static String filterKills(List<JSONObject> playerEvents, String returnFormat, String coordinateFormat,
			int dimensions, String... players) {
		String[] formats = { returnFormat };
		List<String> coordinates = filterAll(playerEvents, formats, coordinateFormat, dimensions, true, false, false,
				false, players);
		assert (!coordinates.isEmpty());
		return coordinates.get(0);
	}

	public static String filterVictims(List<JSONObject> playerEvents, String returnFormat, String coordinateFormat,
			int dimensions, String... players) {
		String[] formats = { returnFormat };
		List<String> coordinates = filterAll(playerEvents, formats, coordinateFormat, dimensions, false, true, false,
				false, players);
		assert (!coordinates.isEmpty());
		return coordinates.get(0);
	}

	private static String[] getCoordinates(JSONObject location, int dimensions) {
		String[] coordinates = new String[dimensions];
		assert (dimensions == 2 || dimensions == 3);
		if (dimensions >= 2) {
			coordinates[0] = "" + location.getDouble("x");
			coordinates[1] = "" + location.getDouble("y");
		}
		if (dimensions == 3) {
			coordinates[2] = "" + location.getDouble("z");
		}

		return coordinates;
	}

	public static List<List<KillSet>> getMultiKills(List<JSONObject> events, boolean includeAcrossDeaths,
			String... players) {
		List<JSONObject> playerEvents = includeAcrossDeaths ? filterPlayerKills(events, players)
				: filterPlayerEvents(events, players);

		List<List<KillSet>> killResult = new ArrayList<List<KillSet>>();

		Map<String, List<JSONObject>> sortedEvents = sortEvents(playerEvents, players);

		for (Map.Entry<String, List<JSONObject>> killsPerPlayer : sortedEvents.entrySet()) {
			Stack<JSONObject> multiKills = new Stack<JSONObject>();
			List<JSONObject> kills = killsPerPlayer.getValue();
			for (int i = 0; i < kills.size(); i++) {
				if (!multiKills.isEmpty() && !isMultiKill(multiKills.peek(), kills.get(i))) {
					// Limit number of arrays to size 10; allow higher
					// multikills, but group them in the same bucket as the 10's
					int multiKillSize = multiKills.size() > 10 ? 10 : multiKills.size();
					if (killResult.size() < multiKillSize) {
						for (int j = killResult.size(); j < multiKillSize; j++) {
							killResult.add(new ArrayList<KillSet>());
						}
					}
					killResult.get(multiKillSize - 1)
							.add(new KillSet(new ArrayList<JSONObject>(multiKills), HaloAPIConstants.DIMENSIONS));

					while (!multiKills.empty()) {
						multiKills.pop();
					}
				}

				if (killsPerPlayer.getKey().equals(getKillerGamertag(kills.get(i)))) {
					multiKills.push(kills.get(i));
				}
			}
		}

		return killResult;
	}

	private static Map<String, List<JSONObject>> sortEvents(List<JSONObject> playerEvents, String... players) {
		Map<String, List<JSONObject>> sortedEvents = new HashMap<String, List<JSONObject>>();
		for (String player : players) {
			sortedEvents.put(player, new ArrayList<JSONObject>());
		}

		for (JSONObject event : playerEvents) {
			String killer = getKillerGamertag(event);
			String victim = getVictimGamertag(event);
			if (killer != null && victim != null) {
				if (sortedEvents.containsKey(killer) && isEnemyKill(event)) {
					sortedEvents.get(killer).add(event);
				}
				if (sortedEvents.containsKey(victim)) {
					sortedEvents.get(victim).add(event);
				}
			}
		}

		return sortedEvents;
	}

	private static boolean isMultiKill(JSONObject previousEvent, JSONObject currentEvent) {
		String killer = getKillerGamertag(previousEvent);
		if (killer == null) {
			killer = "";
		}

		Duration current = Duration.parse(currentEvent.getString("TimeSinceStart"));
		Duration previous = Duration.parse(previousEvent.getString("TimeSinceStart"));

		boolean sameKiller = killer.equals(getKillerGamertag(currentEvent));
		boolean positiveTime = previous.minus(current).toMillis() <= 0;
		boolean multiKill = current.minus(previous).toMillis() / 1000.0 < HaloAPIConstants.MULTI_KILL;
		return sameKiller && positiveTime && multiKill;
	}
}
