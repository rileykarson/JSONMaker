package com.rileykarson.jsonmaker;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONReader {

  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  public static JSONObject readJsonFromUrl(String urlString) throws IOException, JSONException {  
	   URL url = new URL(urlString);
	   HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	   urlConnection.addRequestProperty("Authorization", "OAuth nn9ogaqhspw6mr2y3fiunuat");
	   try {
	     InputStream is = new BufferedInputStream(urlConnection.getInputStream());
	     BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	     String jsonText = readAll(rd);
	     JSONObject json = new JSONObject(jsonText);
	     return json;
	   }
	     finally {
	    	 
	     }
	   }
	  
  
  public static JSONObject controlBoxJSON(String url){ 
	    JSONObject json = null;
		try {
			json = readJsonFromUrl(url);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	    return json;
	  }
  
  public static String controlBox(String url, String id){ 
    JSONObject json = null;
	try {
		json = readJsonFromUrl(url);
	} catch (IOException e) {
		e.printStackTrace();
	} catch (JSONException e) {
		e.printStackTrace();
	}
    try {
		return "" + json.get(id);
	} catch (JSONException e) {
		e.printStackTrace();
		return "failed";
	}
  }
}