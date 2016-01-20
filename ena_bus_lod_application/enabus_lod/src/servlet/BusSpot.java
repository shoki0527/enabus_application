package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import app.SparqlQuery;
import app.Spot;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 * Servlet implementation class Page
 */
@WebServlet("/resource/*")
public class BusSpot extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public BusSpot() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
    	PrintWriter out = response.getWriter();
		StringBuffer reqURI = request.getRequestURL();
		String[] uri = reqURI.toString().split("/");
		String subject = uri[4];



    	out.println("<!--?xml version=1.0 encoding=UTF-8 ?--><!DOCTYPE html PUBLIC -//W3C//DTD XHTML+RDFa 1.0//EN http://www.w3.org/MarkUp/DTD/xhtml-rdfa-1.dtd><html xmlns=http://www.w3.org/1999/xhtml xmlns:rdf=http://www.w3.org/1999/02/22-rdf-syntax-ns# xmlns:rdfs=http://www.w3.org/2000/01/rdf-schema# xmlns:owl=http://www.w3.org/2002/07/owl# xmlns:nejiterms=http://monodzukurilod.org/neji/terms# version=XHTML+RDFa 1.0 xml:lang=ja><head profile=http://www.w3.org/1999/xhtml/vocab><meta http-equiv=Content-Type content=text/html; charset=UTF-8>");
    	out.println("<title>恵那バスLOD</title>");
    	out.println("<link rel=stylesheet href=/css/table.css>");
    	out.println("<script type=text/javascript src=http://maps.google.com/maps/api/js?sensor=false></script></head>");
    	//out.println("<body><h1 align=center>http://localhost:8080/Resource"+ subject + "</h1>");
    	out.println("<body>");
    	//out.println("<!--map--><div id='map' style='width: 600px; height: 400px;'></div><!--map--><script type='text/javascript'>  function attachMessage(marker, msg) {    google.maps.event.addListener(marker, 'click', function(event) {      new google.maps.InfoWindow({        content: msg      }).open(marker.getMap(), marker);    });  }// 位置情報と表示データの組み合わせ  var data = new Array();//マーカー位置の緯度経度  data.push({position: new google.maps.LatLng(35.455067, 137.40869), content: 'ena_ekimae'});  data.push({position: new google.maps.LatLng(35.690921, 139.700258), content: '新宿駅'});   var myMap = new google.maps.Map(document.getElementById('map'), {    zoom: 17,//地図縮尺    center: new google.maps.LatLng(35.455067, 137.40869),//地図の中心点    scrollwheel: false,    mapTypeId: google.maps.MapTypeId.ROADMAP  });   for (i = 0; i < data.length; i++) {    var myMarker = new google.maps.Marker({      position: data[i].position,      map: myMap    });    attachMessage(myMarker, data[i].content);  }</script>");
    	//out.println("<br/><h2 align=center>"+subject+"に関する情報</h2>");
    	/*
    	out.println("<table align=center border=1><tbody><tr><th>Property</th><th>Value</th></tr>");
    	System.out.println(results.hasNext());
		while (results.hasNext()) {
			System.out.println("実行されました１");
			QuerySolution result = results.nextSolution();
		    graph = result.get("graph");
		    p = result.get("p");
		    o = result.get("o");

		    System.out.println(graph + " { " + p + " " + o + " . }");
		    out.println("<tr><td><a href="+p+">"+p+"</a></td><td><span property=rdfs:label xmlns:rdfs=http://www.w3.org/2000/01/rdf-schema#><a href="+o+">"+o+"</a></span></td></tr>");
		}
		*/

    	out.println(find_line(request));

    	String[] subject_sp = subject.split("_");
    	if(subject_sp[subject_sp.length-1].equals("LINE")){
    		//out.println("LINEです");
    		out.println("<h2 align=center>"+subject+"の停留所情報</h2>");

			out.println("<!--map--><div id='map' style='width: 600px; height: 400px;margin:0 auto 0 auto;'></div><!--map-->");
	    	out.println("<script type='text/javascript'>  function attachMessage(marker, msg) {    google.maps.event.addListener(marker, 'click', function(event) {      new google.maps.InfoWindow({        content: msg      }).open(marker.getMap(), marker);    });  }  var data = new Array();");
	    	//路線におけるバス停のフラグ指定
	    	String busstops = LineToBusStop(reqURI.toString());
	    	String[] busstops_sp = busstops.split(",");
	    	int count =0;
	    	String center_lat_long =null;
	    	for(int i=1;i<busstops_sp.length;i++){
	    		String lat_long_busstopname = getBusStopToLatLongBusStopName(busstops_sp[i]);
	    		String[] lat_long_busstopname_sp = lat_long_busstopname.split(",");
	    		out.println("data.push({position: new google.maps.LatLng("+lat_long_busstopname_sp[0]+","+lat_long_busstopname_sp[1]+"), content: '<a href=http://localhost:8080/resource/"+lat_long_busstopname_sp[2]+">"+lat_long_busstopname_sp[2]+"</a>'});");
	    		//strbuf.append(lat_long_spotname_sp[0]+lat_long_spotname_sp[1]+lat_long_spotname_sp[2]+"<br/>");
	    		if(count==0){
	    			center_lat_long =lat_long_busstopname_sp[0]+","+lat_long_busstopname_sp[1];
	    		}
	    	}

	    	out.println("var myMap = new google.maps.Map(document.getElementById('map'), {    zoom: 12,center:");
	    	//中心指定(バス停の座標)
	    	out.println("new google.maps.LatLng("+center_lat_long+"),");

	    	out.println("scrollwheel: false,    mapTypeId: google.maps.MapTypeId.ROADMAP  });   for (i = 0; i < data.length; i++) {    var myMarker = new google.maps.Marker({      position: data[i].position,      map: myMap    });    attachMessage(myMarker, data[i].content);  }</script>");
    	}
    	else{
    		//out.println("LINEじゃないです");
    	}
    	out.println(data_table(request));
    	//out.println(getLatLong(request));
    	//out.println(getSpot(request));

    	//out.println(LineToBusStop("http://localhost:8080/resource/HIRUKAWA_LINE"));
    	//out.println(getBusStopToLatLongBusStopName("http://localhost:8080/resource/Ena_Ekimae"));

    	out.println("</body></html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServlet:Request request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	public static String find_line(HttpServletRequest request){
		StringBuffer strbuf = new StringBuffer();
		ArrayList<String> lines = new ArrayList<String>();
		StringBuffer reqURI = request.getRequestURL();
		String[] uri = reqURI.toString().split("/");
		String subject = uri[4];

		String url = "jdbc:virtuoso://10.29.23.72:1111";
		VirtGraph set = new VirtGraph (url, "dba", "baseball2011");
		Query sparql = QueryFactory.create("PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>"+
				"PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>" +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
				"select ?line ?line_name from <http://localhost:8080/tomita2> where{?line <http://schema.org/arrivalBusStop> <http://localhost:8080/resource/"+subject+">;<http://www.w3.org/2000/01/rdf-schema#label> ?line_name.}");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
		ResultSet results = vqe.execSelect();
		RDFNode graph;
		RDFNode line;
		RDFNode line_name;

		int count =0;
    	//System.out.println(results.hasNext());
		while (results.hasNext()) {
			if(count==0){
				strbuf.append("<br/><h2 align=center>バスの停留所"+subject+"と観光地に関する情報</h2>");
				strbuf.append("<!--map--><div id='map' style='width: 600px; height: 400px;margin:0 auto 0 auto;'></div><!--map-->");
		    	strbuf.append("<script type='text/javascript'>  function attachMessage(marker, msg) {    google.maps.event.addListener(marker, 'click', function(event) {      new google.maps.InfoWindow({        content: msg      }).open(marker.getMap(), marker);    });  }  var data = new Array();");
		    	//バス停フラグ指定
		    	strbuf.append("data.push({position: new google.maps.LatLng("+getLatLong(request)+"), content: '<a href="+reqURI+">"+subject+"</a>'});");
		    	//観光地のフラグ指定
		    	String spot = getSpot(request);
		    	String[] spot_sp = spot.split(",");
		    	for(int i=1;i<spot_sp.length;i++){
		    		String lat_long_spotname = spotToLatLong(spot_sp[i]);
		    		String[] lat_long_spotname_sp = lat_long_spotname.split(",");
		    		strbuf.append("data.push({position: new google.maps.LatLng("+lat_long_spotname_sp[0]+","+lat_long_spotname_sp[1]+"), content: '"+lat_long_spotname_sp[2]+"'});");
		    		//strbuf.append(lat_long_spotname_sp[0]+lat_long_spotname_sp[1]+lat_long_spotname_sp[2]+"<br/>");
		    	}

		    	strbuf.append("var myMap = new google.maps.Map(document.getElementById('map'), {    zoom: 16,center:");
		    	//中心指定(バス停の座標)
		    	strbuf.append("new google.maps.LatLng("+getLatLong(request)+"),");

		    	strbuf.append("scrollwheel: false,    mapTypeId: google.maps.MapTypeId.ROADMAP  });   for (i = 0; i < data.length; i++) {    var myMarker = new google.maps.Marker({      position: data[i].position,      map: myMap    });    attachMessage(myMarker, data[i].content);  }</script>");
				strbuf.append("<table align=center border=1><tbody><tr><td>この停留所に停車する路線</td><td>");
				count++;
			}
			QuerySolution result = results.nextSolution();
		    graph = result.get("graph");
		    line = result.get("line");
		    line_name = result.get("line_name");
		    //String[] line_name_sp = line_name.toString().split("@");
		    //String line_name_view = line_name_sp[line_name_sp.length];
		    strbuf.append("<a href="+line+">"+line_name.toString().replace("_LINE@en", "")+"線</a><br/>");
		}
    	strbuf.append("</td></tr></tbody></table>");
    	return strbuf.toString();
	}

	public static String data_table(HttpServletRequest request){
		StringBuffer strbuf = new StringBuffer();
		StringBuffer reqURI = request.getRequestURL();
		String[] uri = reqURI.toString().split("/");
		String subject = uri[4];
		String url = "jdbc:virtuoso://10.29.23.72:1111";
		VirtGraph set = new VirtGraph (url, "dba", "baseball2011");
		Query sparql = QueryFactory.create("PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>"+
				"PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>" +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
				"select ?p ?o from <http://localhost:8080/tomita2> where{<http://localhost:8080/resource/"+subject+"> ?p ?o.}");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
		ResultSet results = vqe.execSelect();
		RDFNode graph;
		RDFNode p;
		RDFNode o;
		strbuf.append("<br/><h2 align=center>"+subject+"に関する詳細情報</h2>");
    	strbuf.append("<table width=60% align=center border=1><tbody><tr><th>Property</th><th>Value</th></tr>");
    	System.out.println(results.hasNext());
		while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
		    graph = result.get("graph");
		    p = result.get("p");
		    o = result.get("o");
		    strbuf.append("<tr><td><a href="+p+">"+p+"</a></td><td><span property=rdfs:label xmlns:rdfs=http://www.w3.org/2000/01/rdf-schema#><a href="+o+">"+o+"</a></span></td></tr>");
		}
    	strbuf.append("</tbody></table>");
    	return replacer(strbuf.toString());
    	//return strbuf.toString();
	}
	public static String replacer(String original_str){
		String replace_str = original_str.replace(">http://www.w3.org/1999/02/22-rdf-syntax-ns#type</a>",">データタイプ</a>");
		replace_str = replace_str.replace(">http://schema.org/BusStation</a>",">バス停</a>");
		replace_str = replace_str.replace(">http://www.w3.org/2000/01/rdf-schema#label</a>",">名前</a>");
		replace_str = replace_str.replace(">http://www.w3.org/2003/01/geo/wgs84_pos#long</a>",">経度</a>");
		replace_str = replace_str.replace(">http://www.w3.org/2003/01/geo/wgs84_pos#lat</a>",">緯度</a>");
		replace_str = replace_str.replace(">http://www.geonames.org/ontology#nearby</a>",">近隣のスポット</a>");
		replace_str = replace_str.replace("@en","");
		replace_str = replace_str.replace("^^http://www.w3.org/2001/XMLSchema#float","");
		replace_str = replace_str.replace(">http://ja.dbpedia.org/resource/", ">");

		replace_str = replace_str.replace(">http://schema.org/BusTrip</a>",">バス路線</a>");
		replace_str = replace_str.replace(">http://schema.org/arrivalBusStop</a>",">停車するバス停</a>");
		replace_str = replace_str.replace(">http://schema.org/busName</a>",">バス路線名</a>");
		replace_str = replace_str.replace(">http://localhost:8080/resource/",">");
		return replace_str;
	}
	public static String getLatLong(HttpServletRequest request){
		StringBuffer strbuf = new StringBuffer();
		StringBuffer reqURI = request.getRequestURL();
		String[] uri = reqURI.toString().split("/");
		String subject = uri[4];
		String url = "jdbc:virtuoso://10.29.23.72:1111";
		VirtGraph set = new VirtGraph (url, "dba", "baseball2011");
		Query sparql = QueryFactory.create("PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>"+
				"PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>" +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
				"select ?lng ?lat  from <http://localhost:8080/tomita2> where{<http://localhost:8080/resource/"+subject+"> <http://www.w3.org/2003/01/geo/wgs84_pos#long> ?lng;<http://www.w3.org/2003/01/geo/wgs84_pos#lat> ?lat.}");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
		ResultSet results = vqe.execSelect();
		RDFNode graph;
		RDFNode lng;
		RDFNode lat;
		//System.out.println(results.hasNext());
		int count =0;
		while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
		    graph = result.get("graph");
		    lng = result.get("lng");
		    lat = result.get("lat");
		    //strbuf.append("<tr><td><a href="+p+">"+p+"</a></td><td><span property=rdfs:label xmlns:rdfs=http://www.w3.org/2000/01/rdf-schema#><a href="+o+">"+o+"</a></span></td></tr>");
		    if(count==0){
		    	strbuf.append(lat.toString().replace("^^http://www.w3.org/2001/XMLSchema#float", "")+",");
		    	strbuf.append(lng.toString().replace("^^http://www.w3.org/2001/XMLSchema#float", ""));
		    	count++;
		    }
		}
		return strbuf.toString();
	}
	//近隣のスポット一覧を調べる
	public static String getSpot(HttpServletRequest request){
		StringBuffer strbuf = new StringBuffer();
		StringBuffer reqURI = request.getRequestURL();
		String[] uri = reqURI.toString().split("/");
		String subject = uri[4];
		String url = "jdbc:virtuoso://10.29.23.72:1111";
		VirtGraph set = new VirtGraph (url, "dba", "baseball2011");
		Query sparql = QueryFactory.create("PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>"+
				"PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>" +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
				"select ?spot  from <http://localhost:8080/tomita2> where{<http://localhost:8080/resource/"+subject+"> <http://www.geonames.org/ontology#nearby> ?spot.}");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
		ResultSet results = vqe.execSelect();
		RDFNode graph;
		RDFNode spot;
		//System.out.println(results.hasNext());
		while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
		    graph = result.get("graph");
		    spot = result.get("spot");
		    //strbuf.append("<tr><td><a href="+p+">"+p+"</a></td><td><span property=rdfs:label xmlns:rdfs=http://www.w3.org/2000/01/rdf-schema#><a href="+o+">"+o+"</a></span></td></tr>");
		    strbuf.append(","+spot.toString());

		}
		return strbuf.toString();
	}
	//近隣のスポット名から緯度、経度、名前を求める
	public static String spotToLatLong(String uri){
		StringBuffer lat_long_spotname = new StringBuffer();
		try {
			String spot_json = SparqlQuery.HttpMethod("GET", new URI(uri).toASCIIString());
			lat_long_spotname.append(Spot.latParser(spot_json, uri)+",");
			lat_long_spotname.append(Spot.longParser(spot_json, uri)+",");
			lat_long_spotname.append(Spot.labelParser(spot_json, uri));
			return lat_long_spotname.toString();
		} catch (URISyntaxException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;
	}
	//バス停から緯度経度を求める
	public static String getBusStopToLatLongBusStopName(String bus_stop){
		String[] uri = bus_stop.toString().split("/");
		String subject = uri[4];
		StringBuffer lat_long_busstopname = new StringBuffer();
		String url = "jdbc:virtuoso://10.29.23.72:1111";
		VirtGraph set = new VirtGraph (url, "dba", "baseball2011");
		Query sparql = QueryFactory.create("PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>"+
				"PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>" +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
				"select ?lat ?lng  from <http://localhost:8080/tomita2> where{<http://localhost:8080/resource/"+subject+"> <http://www.w3.org/2003/01/geo/wgs84_pos#lat> ?lat;<http://www.w3.org/2003/01/geo/wgs84_pos#long> ?lng.}");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
		ResultSet results = vqe.execSelect();
		RDFNode graph;
		RDFNode lat;
		RDFNode lng;
		//System.out.println(results.hasNext());
		int count =0;
		while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
		    graph = result.get("graph");
		    lat = result.get("lat");
		    lng = result.get("lng");
		    //strbuf.append("<tr><td><a href="+p+">"+p+"</a></td><td><span property=rdfs:label xmlns:rdfs=http://www.w3.org/2000/01/rdf-schema#><a href="+o+">"+o+"</a></span></td></tr>");
		    if(count==0){
		    	lat_long_busstopname.append(lat.toString()+",");
		    	lat_long_busstopname.append(lng.toString()+",");
		    	lat_long_busstopname.append(subject);
		    	count++;
		    }

		}
		return lat_long_busstopname.toString().replace("^^http://www.w3.org/2001/XMLSchema#float", "");
	}
	//路線名から停車するバス停一覧を取得
	public static String LineToBusStop(String line){
		String[] uri = line.toString().split("/");
		String subject = uri[4];
		StringBuffer bus_stops = new StringBuffer();
		String url = "jdbc:virtuoso://10.29.23.72:1111";
		VirtGraph set = new VirtGraph (url, "dba", "baseball2011");
		Query sparql = QueryFactory.create("PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>"+
				"PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>" +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
				"select ?busstop  from <http://localhost:8080/tomita2> where{<http://localhost:8080/resource/"+subject+"> <http://schema.org/arrivalBusStop> ?busstop.}");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
		ResultSet results = vqe.execSelect();
		RDFNode graph;
		RDFNode busstop;
		//System.out.println(results.hasNext());
		while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
		    graph = result.get("graph");
		    busstop = result.get("busstop");
		    //strbuf.append("<tr><td><a href="+p+">"+p+"</a></td><td><span property=rdfs:label xmlns:rdfs=http://www.w3.org/2000/01/rdf-schema#><a href="+o+">"+o+"</a></span></td></tr>");
		    	bus_stops.append(","+busstop.toString());
		}
		return bus_stops.toString();
	}
}

