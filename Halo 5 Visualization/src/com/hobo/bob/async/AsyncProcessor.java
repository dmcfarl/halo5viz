package com.hobo.bob.async;

import java.io.IOException;

import org.glassfish.jersey.server.ChunkedOutput;
import org.json.JSONException;
import org.json.JSONObject;

public interface AsyncProcessor {
	void process(ChunkedOutput<String> output, JSONObject matchEvents, String outputFormat, String... players) throws JSONException, IOException;
}
