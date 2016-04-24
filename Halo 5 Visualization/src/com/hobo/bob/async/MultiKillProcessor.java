package com.hobo.bob.async;

import java.io.IOException;
import java.util.List;

import org.glassfish.jersey.server.ChunkedOutput;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hobo.bob.haloapi.filter.StatsFilter;
import com.hobo.bob.model.KillSet;

public class MultiKillProcessor implements AsyncProcessor {

	@Override
	public void process(ChunkedOutput<String> output, JSONObject matchEvents, String outputFormat, String... players) throws JSONException, IOException {
		List<List<KillSet>> killSets = StatsFilter.getMultiKills(matchEvents, true, players);
		JSONArray js = new JSONArray();
		for (int i = 0; i < killSets.size(); i++) {
			List<KillSet> multiKillSet = killSets.get(i);
			JSONArray multiKillJS = new JSONArray();
			for (KillSet kills : multiKillSet) {
				multiKillJS.put(kills.getOutput(true, true));
			}
			js.put(multiKillJS);
		}
		output.write(String.format(outputFormat, js.toString(4)));
	}

}
