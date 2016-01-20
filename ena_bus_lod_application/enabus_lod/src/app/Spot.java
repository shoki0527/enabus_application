package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Spot {
	private String bus_stop;//バス停情報を表すURI
	private String spot;//dbpedia上の観光地情報を表すURI
	private String spot_json;//dbpedia上の観光地情報をjson形式で取得した内容
	private String label;//スポット名
	private String comment;//dbepadi上の観光地紹介 abstract
	private String address;//観光地の住所

	public Spot(String bus_stop,String spot) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.bus_stop = bus_stop;this.spot = spot;
		init();
	}
	/*
	 * dbpediaから観光地情報を取得する
	 */
	private void init(){
		try {
			this.spot_json = HttpMethod("GET", new URI(this.spot).toASCIIString());//観光地情報をjson形式で取得して格納
			this.comment = commentParser(this.spot_json,this.spot);
			this.label = labelParser(this.spot_json,this.spot);
			this.address = AddressParser(this.spot_json,this.spot);


		} catch (URISyntaxException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	public String getBus_stop() {
		return bus_stop;
	}
	public String getSpot() {
		return spot;
	}
	public void setBus_stop(String bus_stop) {
		this.bus_stop = bus_stop;
	}
	public void setSpot(String spot) {
		this.spot = spot;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getAddress() {
		return address;
	}
	public String getLabel() {
		return label;
	}
	public String getSpot_json() {
		return spot_json;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public void setSpot_json(String spot_json) {
		this.spot_json = spot_json;
	}
	private String commentParser(String json, String uri) {
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

	private String AddressParser(String json, String uri) {
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
	public static String HttpMethod(String method, String url){
		String resp = null;
		try {
            URL URL = new URL(url);

            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) URL.openConnection();
                connection.setRequestMethod(method);
                connection.setRequestProperty("accept", "application/json");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    try (InputStreamReader isr = new InputStreamReader(connection.getInputStream(),
                                                                       StandardCharsets.UTF_8);
                         BufferedReader reader = new BufferedReader(isr)) {
                    	String buf;
                    	resp = buf = reader.readLine();
                        while ((buf = reader.readLine()) != null) {
                            resp += buf;
                        }
                    }
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		return resp;
	}

	public static String latParser(String json, String uri) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(json);
			JsonNode currentNode = rootNode.get(uri);
			JsonNode currentNode2 = currentNode.get("http://www.w3.org/2003/01/geo/wgs84_pos#lat");
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
	public static String longParser(String json, String uri) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(json);
			JsonNode currentNode = rootNode.get(uri);
			JsonNode currentNode2 = currentNode.get("http://www.w3.org/2003/01/geo/wgs84_pos#long");
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
