package com.hobo.bob.haloapi;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hobo.bob.util.ReadKey;

public class APICall {
	public static JSONObject callObject(String url) throws URISyntaxException, ClientProtocolException, IOException, JSONException, InterruptedException {
		return new JSONObject(call(url));
	}
	
	public static JSONArray callArray(String url) throws URISyntaxException, ClientProtocolException, IOException, JSONException, InterruptedException {
		return new JSONArray(call(url));
	}
	
	public static String call(String url) throws URISyntaxException, ClientProtocolException, IOException, InterruptedException {
		System.out.println(url);
		String result = null;
		// TODO close client
		HttpClient httpclient = HttpClients.createDefault();

		URIBuilder builder = new URIBuilder(url);

		URI uri = builder.build();
		HttpGet request = new HttpGet(uri);
		request.setHeader("Ocp-Apim-Subscription-Key", ReadKey.getInstance().getKey());

		HttpResponse response = httpclient.execute(request);
		HttpEntity entity = response.getEntity();
		
		if (entity != null) {
			result = EntityUtils.toString(entity);
		}
		
		System.out.println("\"" + result.substring(0, result.length() > 100 ? 100 : result.length()) + "\"");
		
		if (result.contains("\"message\": \"Rate limit is exceeded")) {
			Thread.sleep(5000);
			result = call(url);
		}
		
		return result;
	}
}
