package com.hobo.bob;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.hobo.bob.haloapi.StatsCall;
import com.hobo.bob.haloapi.filter.MetadataFilter;
import com.hobo.bob.haloapi.filter.StatsFilter;

/**
 * Servlet implementation class RetrieveStats
 */
@WebServlet("/RetrieveStats")
public class RetrieveStats extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RetrieveStats() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] players = request.getParameterValues("player");
		String mapId = request.getParameter("mapId");
		String map = request.getParameter("map");
		String numMatches = request.getParameter("numMatches");
		

		String playerStr = "";
		for (int i = 0; i < players.length; i++) {
			players[i] = URLDecoder.decode(players[i], "UTF-8");
			playerStr += "\"" + players[i] + "\"\t";
		}
		
		if (mapId == null && map != null) {
			mapId = MetadataFilter.getMapId(map);
		}

		System.out.println("players = " + playerStr);
		System.out.println("map = \"" + mapId + "\" (" + map + ")");
		System.out.println("numMatches = \"" + numMatches + "\"");
		
		if (numMatches == null) {
			numMatches = "1";
		}

		List<JSONObject> events = StatsCall.getMatchEvents(HaloAPIConstants.WARZONE_MODE, mapId,
				Integer.parseInt(numMatches), players);
		List<JSONObject> playerEvents = StatsFilter.filterPlayerKills(events, players);
		String coordinates = StatsFilter.filterVictims(playerEvents, "[%s]", "[%s,%s]", 2, players);

		response.setContentType("application/json");
		response.getWriter().append(coordinates);
	}

}
