package com.hobo.bob;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ChunkedOutput;

import com.hobo.bob.async.MultiKillProcessor;
import com.hobo.bob.async.RetrieveMatches;
import com.hobo.bob.haloapi.MetadataCall;
import com.hobo.bob.haloapi.filter.MetadataFilter;
/**
 * Restful Web Service implementation class RetrieveStats
 */
@Path("/RetrieveStats")
public class RetrieveStats {

	/*@Path("/Events")
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
	
	@Path("/Heatmap")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getHeatmap(@QueryParam("player") Set<String> playerSet, @QueryParam("mode") String mode,
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
		List<List<KillSet>> multiKills = StatsFilter.getMultiKills(events, true, players);
		JSONArray js = new JSONArray();
		for (int i = 2; i < multiKills.size() + 1; i++) {
			List<KillSet> multiKillSet = multiKills.get(i);
			JSONArray output = new JSONArray();
			for (KillSet kills : multiKillSet) {
				output.put(kills.getOutput(true, false));
			}
			js.put(output);
		}
		return js.toString(4);
	}*/
	
	@Path("/MultiKills")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ChunkedOutput<String> getMultiKills(@QueryParam("player") Set<String> playerSet,
			@QueryParam("mode") String mode, @QueryParam("mapName") String mapName, @QueryParam("mapId") String mapId,
			@QueryParam("numMatches") int numMatches) throws UnsupportedEncodingException {
		final ChunkedOutput<String> output = new ChunkedOutput<String>(String.class);

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

		new Thread(new RetrieveMatches(output, new MultiKillProcessor(), mode, mapId, numMatches, players)).start();

		return output;
	}
	
	@Path("/MapNames")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getMaps() {
		return MetadataCall.getMaps().toString(4);
	}

}
