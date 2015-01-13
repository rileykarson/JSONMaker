package com.rileykarson.jsonmaker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONMaker {

	public static void main(String[] args){
		String url = "https://apitest-api.crystalcommerce.com/v1/products/54983?embedded=variants";
		JSONObject root = JSONReader.controlBoxJSON(url);
		makeFormattedFile(root);
	}
	
	private static void makeFormattedFile(JSONObject root) {
		JSONObject format = loadJSONObjectFromFile("format");
		String cardSEOName = getCardSEOName(root);
		if (cardSEOName.endsWith("__foil")){
			cardSEOName = cardSEOName.replace("__foil", "");
		}
		File f = new File(cardSEOName + ".json");
		if (f.exists()){
			format = addVariants(root,cardSEOName);
			
		} else {
			format = populateFormat(format, root);
		}
		try {
			f.createNewFile();
			PrintWriter out = new PrintWriter(cardSEOName + ".json");
			out.println(format.toString());
			out.close();
		} catch (IOException e) {
			System.out.println("Error making file!");
			e.printStackTrace();
		}
	}

	private static JSONObject addVariants(JSONObject root, String name) {	
		try {
			JSONObject card = loadJSONObjectFromFile(name);
			JSONArray variants = card.getJSONArray("variants");
			int cardArrayLength = root.getJSONObject("product").getJSONObject("_embedded").getJSONArray("variants").length();
			int i = 0;
			while (i < cardArrayLength){
				JSONObject index = root.getJSONObject("product").getJSONObject("_embedded").getJSONArray("variants").getJSONObject(i).getJSONObject("variant");
				JSONObject newIndex = new JSONObject();
				newIndex.put("productID", index.get("id"));
				newIndex.put("catalogID", index.get("product_id"));
				newIndex.put("cardPrice", index.getJSONObject("sell_price").getJSONObject("money").get("cents"));
				newIndex.put("buyListPrice", index.getJSONObject("buy_price").getJSONObject("money").get("cents"));
				newIndex.put("cardQuantity", index.get("qty"));
				newIndex.put("cardBuyQuantity", index.get("wtb_qty"));
				newIndex.put("cardName", index.get("product_name"));
				newIndex.put("cardSet", index.get("category_name"));
				newIndex.put("cardCondition", index.getJSONArray("descriptors").getJSONObject(0).getJSONObject("variant_descriptor").get("value"));
				newIndex.put("cardLanguage", index.getJSONArray("descriptors").getJSONObject(1).getJSONObject("variant_descriptor").get("value"));
				newIndex.put("cardFinish", ""+root.getJSONObject("product").getJSONArray("descriptors").getJSONObject(9).getJSONObject("product_descriptor").get("value"));
				variants.put(newIndex);
				i = i + 1;
			}
			card.put("variants", variants);
			return card;
		} catch (JSONException e) {
			System.out.println("Failure in adding variants!");
			return null;
		}
		
		
	}

	private static JSONObject populateFormat(JSONObject format, JSONObject root) {
		try {
			String cardName =  root.getJSONObject("product").getString("name");
			String cardSEOName = root.getJSONObject("product").getString("seoname");
			if (cardSEOName.endsWith("__foil")){
				cardSEOName = cardSEOName.replace("__foil", "");
				cardName = cardName.replace(" - Foil", "");
			}
			format.put("cardSEOName", cardSEOName);
			format.put("cardName", cardName);
			
			
			JSONObject staticData = format.getJSONObject("staticData"); //Static Data!
			staticData.put("cardColor", ""+root.getJSONObject("product").getJSONArray("descriptors").getJSONObject(0).getJSONObject("product_descriptor").get("value"));
			staticData.put("cardFlavorText", ""+root.getJSONObject("product").getJSONArray("descriptors").getJSONObject(1).getJSONObject("product_descriptor").get("value"));
			staticData.put("cardRulesText", ""+root.getJSONObject("product").getJSONArray("descriptors").getJSONObject(2).getJSONObject("product_descriptor").get("value"));
			staticData.put("cardRarity", ""+root.getJSONObject("product").getJSONArray("descriptors").getJSONObject(3).getJSONObject("product_descriptor").get("value"));
			staticData.put("cardCost", ""+root.getJSONObject("product").getJSONArray("descriptors").getJSONObject(4).getJSONObject("product_descriptor").get("value"));
			staticData.put("cardPowerToughness", ""+root.getJSONObject("product").getJSONArray("descriptors").getJSONObject(5).getJSONObject("product_descriptor").get("value"));
			staticData.put("cardType", ""+root.getJSONObject("product").getJSONArray("descriptors").getJSONObject(6).getJSONObject("product_descriptor").get("value"));
			staticData.put("cardArtist", ""+root.getJSONObject("product").getJSONArray("descriptors").getJSONObject(7).getJSONObject("product_descriptor").get("value"));
			format.put("staticData", staticData);
			
			//TODO: Make it do it for variants
			JSONArray variants = format.getJSONArray("variants");
			int cardArrayLength = root.getJSONObject("product").getJSONObject("_embedded").getJSONArray("variants").length();
			int i = 0;
			while (i < cardArrayLength){
				JSONObject index = root.getJSONObject("product").getJSONObject("_embedded").getJSONArray("variants").getJSONObject(i).getJSONObject("variant");
				JSONObject newIndex = new JSONObject();
				newIndex.put("productID", index.get("id"));
				newIndex.put("catalogID", index.get("product_id"));
				newIndex.put("cardPrice", index.getJSONObject("sell_price").getJSONObject("money").get("cents"));
				newIndex.put("buyListPrice", index.getJSONObject("buy_price").getJSONObject("money").get("cents"));
				newIndex.put("cardQuantity", index.get("qty"));
				newIndex.put("cardBuyQuantity", index.get("wtb_qty"));
				newIndex.put("cardName", index.get("product_name"));
				newIndex.put("cardSet", index.get("category_name"));
				newIndex.put("cardCondition", index.getJSONArray("descriptors").getJSONObject(0).getJSONObject("variant_descriptor").get("value"));
				newIndex.put("cardLanguage", index.getJSONArray("descriptors").getJSONObject(1).getJSONObject("variant_descriptor").get("value"));
				newIndex.put("cardFinish", ""+root.getJSONObject("product").getJSONArray("descriptors").getJSONObject(9).getJSONObject("product_descriptor").get("value"));
				variants.put(newIndex);
				i = i + 1;
			}
			variants.remove(0);
			format.put("variants", variants);
			return format;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String getCardSEOName(JSONObject root) {
		JSONObject product;
		String seoname = null;
		try {
			product = root.getJSONObject("product");
			seoname = product.getString("seoname");
		} catch (JSONException e) {
			System.out.println("Failed retrieving JSON data.");
			e.printStackTrace();
		}
		return seoname;
	}

	private static JSONObject loadJSONObjectFromFile(String name) {
		Scanner in;
		try {
			in = new Scanner(new FileReader(name + ".json"));
			String scannerOutput = in.nextLine();
			in.close();
			JSONObject json = new JSONObject(scannerOutput);
			return json;
		} catch (FileNotFoundException e) {
			System.out.println("File not found! Null returned.");
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			System.out.println("JSONObject not made correctly! Null returned.");
			e.printStackTrace();
			return null;
		}
		
	}

	public static void JSONMaker(JSONObject cardData, int index){
		String seoname;
		JSONObject product = null;
		try {
			JSONObject collection = cardData.getJSONObject("paginated_collection");
			JSONArray entries = collection.getJSONArray("entries"); 
			JSONObject arrayIndex = entries.getJSONObject(index);
			product = arrayIndex.getJSONObject("product");
			seoname = product.getString("seoname");
		} catch (JSONException e) {
			seoname = "";
			e.printStackTrace();
		} 
		File f = new File(seoname + ".json");
		
		if(f.exists()){
			System.out.println("File exists!");
			Scanner in = null;
			JSONObject oldFile = null;
			try {
				in = new Scanner(new FileReader(seoname + ".json"));
				oldFile = new JSONObject(in);
				in.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//Add data to 'variants' array
			f.delete();
			try {
				f.createNewFile();
				PrintWriter out = new PrintWriter(seoname+".json");
				out.println(product.toString());
				out.close();
				System.out.println(product.toString());
				System.out.println(seoname +".json was rewritten successfully!");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		try {
			f.createNewFile();
			PrintWriter out = new PrintWriter(seoname + ".json");
			out.println(product.toString());
			out.close();
			System.out.println(seoname +".json was written successfully!");
		} catch (IOException e) {	
			e.printStackTrace();
		}
	}
}

