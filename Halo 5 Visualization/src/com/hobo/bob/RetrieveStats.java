package com.hobo.bob;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
//import java.util.Set;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.hobo.bob.haloapi.StatsCall;
import com.hobo.bob.haloapi.filter.MetadataFilter;
import com.hobo.bob.haloapi.filter.StatsFilter;

/**
 * Restful Web Service implementation class RetrieveStats
 */
@Path("/RetrieveStats")
public class RetrieveStats {

	@Path("/Events")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getEvents(@QueryParam("player") Set<String> playerSet, @QueryParam("mode") String mode,
			@QueryParam("mapName") String mapName, @QueryParam("mapId") String mapId,
			@QueryParam("numMatches") int numMatches) throws UnsupportedEncodingException {

		String[] players = playerSet.toArray(new String[playerSet.size()]);
		String playerStr = "";
		for (int i = 0; i < players.length; i++) {
			players[i] = URLDecoder.decode(players[i], "UTF-8");
			playerStr += "\"" + players[i] + "\"\t";
		}

		if (mapId == null && mapName != null) {
			mapId = MetadataFilter.getMapId(mapName);
		} else if (mapName == null) {
			mapName = MetadataFilter.getMapName(mapId);
		} else {
			throw new IllegalArgumentException("Must filter by mapName or mapId parameter.");
		}

		if (mode == null) {
			mode = HaloAPIConstants.WARZONE_MODE;
		}

		System.out.println("players = " + playerStr);
		System.out.println("map = \"" + mapId + "\" (" + mapName + ")");
		System.out.println("numMatches = \"" + numMatches + "\"");

		List<JSONObject> events = StatsCall.getMatchEvents(mode, mapId, numMatches, players);
		List<JSONObject> playerEvents = StatsFilter.filterPlayerKills(events, players);
		return StatsFilter.filterVictims(playerEvents, "[%s]", "[%s,%s]", 2, players);
	}

	@Path("/MultiKills")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getMultiKills(@QueryParam("player") Set<String> playerSet, @QueryParam("mode") String mode,
			@QueryParam("mapName") String mapName, @QueryParam("mapId") String mapId,
			@QueryParam("numMatches") int numMatches) throws UnsupportedEncodingException {
		String[] players = playerSet.toArray(new String[playerSet.size()]);

		String playerStr = "";
		for (int i = 0; i < players.length; i++) {
			players[i] = URLDecoder.decode(players[i], "UTF-8");
			playerStr += "\"" + players[i] + "\"\t";
		}

		if (mapId == null && mapName != null) {
			mapId = MetadataFilter.getMapId(mapName);
		} else if (mapName == null) {
			mapName = MetadataFilter.getMapName(mapId);
		} else {
			throw new IllegalArgumentException("Must filter by mapName or mapId parameter.");
		}

		if (mode == null) {
			mode = HaloAPIConstants.WARZONE_MODE;
		}

		System.out.println("players = " + playerStr);
		System.out.println("map = \"" + mapName + "\" (" + mapId + ")");
		System.out.println("numMatches = \"" + numMatches + "\"");

		List<JSONObject> events = StatsCall.getMatchEvents(mode, mapId, numMatches, players);
		Map<String, List<List<JSONObject>>> multiKills = StatsFilter.getMultiKills(events, true, players);
		StringBuffer js = new StringBuffer("[");
		for (int i = 2; i < multiKills.size() + 1; i++) {
			String multiKillId = String.format(HaloAPIConstants.MULTI_KILL_VAR_FORMAT, i);
			List<List<JSONObject>> multiKill = multiKills.get(multiKillId);
			List<JSONObject> kills = new ArrayList<JSONObject>();
			for (List<JSONObject> sequence : multiKill) {
				kills.addAll(sequence);
			}
			String coordinates = StatsFilter.filterKills(kills, "[%s]", "[%s,%s]", 2, players);
			js.append(coordinates);
			js.append(",\n");
		}
		js.delete(js.length() - 2, js.length());
		js.append("]");
		return js.toString();
	}

}
