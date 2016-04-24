package com.hobo.bob.async;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.glassfish.jersey.server.ChunkedOutput;
import org.json.JSONArray;
import org.json.JSONObject;

import com.hobo.bob.haloapi.StatsCall;
import com.hobo.bob.haloapi.filter.StatsFilter;

public class RetrieveMatches implements Runnable {

	final ChunkedOutput<String> output;
	final String mode;
	final String mapId;
	final int numMatchesPerPlayer;
	final String[] players;
	final AsyncProcessor processor;

	public RetrieveMatches(ChunkedOutput<String> output, AsyncProcessor processor, String mode, String mapId,
			int numMatchesPerPlayer, String... players) {
		this.output = output;
		this.processor = processor;
		this.mode = mode;
		this.mapId = mapId;
		this.numMatchesPerPlayer = numMatchesPerPlayer;
		this.players = players;
	}

	@Override
	public void run() {
		try {
			Map<String, JSONObject> result = new HashMap<String, JSONObject>();
			String outputFormat = "[%s";
			for (String player : players) {
				Map<String, JSONObject> playerResult = new HashMap<String, JSONObject>();

				try {
					int retries = 0;
					int toRetrieve = numMatchesPerPlayer * 5 > 25 ? 25 : numMatchesPerPlayer * 5;
					int retryMax = numMatchesPerPlayer * 10;
					int retrieved = toRetrieve;
					int matchesProcessed = 0;

					while (matchesProcessed < numMatchesPerPlayer && retries < retryMax && retrieved > 0) {
						try {
							JSONObject response = StatsCall.getMatchList(mode, mapId, (retries * toRetrieve + 1),
									toRetrieve, player);

							JSONArray resultsArray = response.getJSONArray("Results");
							for (int i = 0; i < resultsArray.length()
									&& playerResult.size() < numMatchesPerPlayer; i++) {
								try {
									JSONObject match = resultsArray.getJSONObject(i);
									if (StatsFilter.getMapId(match).equals(mapId)) {
										System.out.println("Found mapId!");
										String matchId = StatsFilter.getMatchId(match);
										if (!playerResult.containsKey(matchId)) {
											JSONObject events = StatsCall.getMatchEvents(matchId);
											if (events != null) {
												matchesProcessed++;
												playerResult.put(matchId, events);
												// TODO Multithread?
												processor.process(output, events, outputFormat, players);
												outputFormat = ",%s";
											}
										} else {
											matchesProcessed++;
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
			output.write("]");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
