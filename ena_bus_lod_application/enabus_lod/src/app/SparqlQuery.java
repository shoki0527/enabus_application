package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class SparqlQuery {


	public static ArrayList<Spot> getSpotAndBusstop(String BaseURI,String SensorID,String ObservationID){
		//String NodeURI = "http://localhost:8080/Resource/0001/SensorOutput0";
		//String PropertyURI = "http://www.w3.org/2005/Incubator/ssn/ssnx/ssn#hasValue";
		ArrayList<Spot> ans = new ArrayList<Spot>();

		String DBurl = "jdbc:virtuoso://10.29.23.72:1111";
		VirtGraph set = new VirtGraph (DBurl, "dba", "baseball2011");
		Query sparql = QueryFactory.create("PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>"+
				"PREFIX loh: <http://localhost:8080/Sensor/Resource/>" +
				"PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>" +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
				//"select ?o from <http://sensorsensor.com> where { <"+BaseURI+SensorID+"/"+ObservationID+"> <http://www.w3.org/2005/Incubator/ssn/ssnx/ssn#observedProperty> ?a.?a <http://www.w3.org/2000/01/rdf-schema#label> ?o.}");
				"select * from<http://localhost:8080/tomita2> where{?bus_stop <http://www.geonames.org/ontology#nearby> ?spot.}");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
		ResultSet results = vqe.execSelect();
		RDFNode graph;
		RDFNode bus_stop = null;
		RDFNode spot = null;
		while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
		    graph = result.get("graph");
		    bus_stop = result.get("bus_stop");
		    spot = result.get("spot");
		    if(bus_stop!=null&&spot!=null)
		    	ans.add(new Spot(bus_stop.toString(),spot.toString()));
		    //System.out.println(spot+"  :  "+bus_stop);

		}

		//String[] ansArray = ans.toString().split("@");
		//ここ場合によっては、エラー出そうな予感
		   return ans;
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

}
