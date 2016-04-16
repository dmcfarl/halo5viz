package com.hobo.bob;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
 * Servlet implementation class RetrieveMultiKills
 */
@WebServlet("/RetrieveMultiKills")
public class RetrieveMultiKills extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RetrieveMultiKills() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] players = request.getParameterValues("player");
		String map = request.getParameter("map");
		String numMatches = request.getParameter("numMatches");

		String playerStr = "";
		for (int i = 0; i < players.length; i++) {
			players[i] = URLDecoder.decode(players[i], "UTF-8");
			playerStr += "\"" + players[i] + "\"\t";
		}
		
		String mapId = MetadataFilter.getMapId(map);

		System.out.println("players = " + playerStr);
		System.out.println("map = \"" + map + "\" (" + mapId + ")");
		System.out.println("numMatches = \"" + numMatches + "\"");

		List<JSONObject> events = StatsCall.getMatchEvents(HaloAPIConstants.WARZONE_MODE, mapId,
				Integer.parseInt(numMatches), players);
		Map<String, List<List<JSONObject>>> multiKills = StatsFilter.getMultiKills(events, true, players);
		StringBuffer js = new StringBuffer("[");
		for (int i = 2; i < multiKills.size() + 1; i++) {
			String multiKillId = String.format(HaloAPIConstants.MULTI_KILL_VAR_FORMAT, i);
			List<List<JSONObject>> multiKill = multiKills.get(multiKillId);
			List<JSONObject> kills = new ArrayList<JSONObject>();
			for (List<JSONObject> sequence : multiKill) {
				kills.addAll(sequence);
			}
			String coordinates = StatsFilter.filterKills(kills, "[%s]", "[%s,%s]", 2, HaloAPIConstants.PLAYER);
			js.append(coordinates);
			js.append(",\n");
		}
		js.delete(js.length() - 2, js.length());
		js.append("]");
		String coordinates = js.toString();

		response.setContentType("application/json");
		response.getWriter().append(coordinates);
	}

}
