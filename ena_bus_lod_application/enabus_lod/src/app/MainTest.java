package app;

import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MainTest {
	public static void main(String[] args){
		ArrayList<Spot> spotsList = SparqlQuery.getSpotAndBusstop("aa", "aa", "aa");
		Iterator<Spot> it = spotsList.iterator();
		while(it.hasNext()){
			Spot spot = it.next();
			System.out.println("------------------------------------------");
			System.out.println(spot.getLabel());
			System.out.println(spot.getComment());
			System.out.println(spot.getAddress());
			System.out.println(spot.getBus_stop());
			System.out.println("------------------------------------------");
		}
	}
	/*
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		ArrayList<Spot> spot = SparqlQuery.getSpotAndBusstop("safd", "adf",
				"sdfs");
		Iterator<Spot> it = spot.iterator();
		while (it.hasNext()) {
			//it.next().getSpot();
			// System.out.println(it.next().getSpot());
			try {
				Spot sp = it.next();
				String uri = sp.getSpot();
				//String uri = "http://ja.dbpedia.org/resource/横井照子ひなげし美術館";
				String uri2 = new URI(uri).toASCIIString();

				// System.out.println(uri2);
				String original_json = SparqlQuery.HttpMethod("GET", uri2);
				String comment_json = commentParser(original_json,uri);
				String label_json = labelParser(original_json,uri);
				String address_json = AddressParser(original_json,uri);
				// System.out.println(json);
				if(comment_json!=null)
					System.out.println(address_json+label_json+sp.getBus_stop()+ ";;;;;"+comment_json);
			} catch (Exception e) {
			}
		}

	}
	*/

	public static String commentParser(String json, String uri) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(json);
			JsonNode currentNode = rootNode.get(uri);
			JsonNode currentNode2 = currentNode.get("http://dbpedia.org/ontology/abstract");
			String comment = currentNode2.toString();
			String sp_comment[] = comment.split(",");
			comment = sp_comment[1];
			sp_comment= comment.split(":");
			comment = sp_comment[1].replace("\"", "");
			//System.out.println(currentNode2.toString());
			/*
			Iterator<String> fieldNames = currentNode2.fieldNames();
			while (fieldNames.hasNext()) {
				String name = fieldNames.next();
				String value = rootNode.path(name).toString();
				//if(value!="null")
				//	System.out.println(name + ":" + value);
			}
			*/
			return comment;
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;
	}
	public static String labelParser(String json, String uri) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(json);
			JsonNode currentNode = rootNode.get(uri);
			JsonNode currentNode2 = currentNode.get("http://www.w3.org/2000/01/rdf-schema#label");
			String comment = currentNode2.toString();
			String sp_comment[] = comment.split(",");
			comment = sp_comment[1];
			sp_comment= comment.split(":");
			comment = sp_comment[1].replace("\"", "");
			//System.out.println(currentNode2.toString());
			/*
			Iterator<String> fieldNames = currentNode2.fieldNames();
			while (fieldNames.hasNext()) {
				String name = fieldNames.next();
				String value = rootNode.path(name).toString();
				//if(value!="null")
				//	System.out.println(name + ":" + value);
			}
			*/
			return comment;
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;
	}

	public static String AddressParser(String json, String uri) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(json);
			JsonNode currentNode = rootNode.get(uri);
			JsonNode currentNode2 = currentNode.get("http://ja.dbpedia.org/property/所在地");
			String comment = currentNode2.toString();
			String sp_comment[] = comment.split(",");
			comment = sp_comment[1];
			sp_comment= comment.split(":");
			comment = sp_comment[1].replace("\"", "");
			//System.out.println(currentNode2.toString());
			/*
			Iterator<String> fieldNames = currentNode2.fieldNames();
			while (fieldNames.hasNext()) {
				String name = fieldNames.next();
				String value = rootNode.path(name).toString();
				//if(value!="null")
				//	System.out.println(name + ":" + value);
			}
			*/
			return comment;
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;
	}
}