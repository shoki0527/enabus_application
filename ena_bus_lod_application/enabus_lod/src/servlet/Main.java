package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.SparqlQuery;
import app.Spot;

/**
 * Servlet implementation class Main
 */
@WebServlet("/app")
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Main() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out;
		ArrayList<Spot> spotsList = SparqlQuery.getSpotAndBusstop("aa", "aa",
				"aa");
		Iterator<Spot> it = spotsList.iterator();
		response.setContentType("text/html; charset=Shift_JIS");

		out = response.getWriter();
    	out.println("<!--?xml version=1.0 encoding=UTF-8 ?--><!DOCTYPE html PUBLIC -//W3C//DTD XHTML+RDFa 1.0//EN http://www.w3.org/MarkUp/DTD/xhtml-rdfa-1.dtd><html xmlns=http://www.w3.org/1999/xhtml xmlns:rdf=http://www.w3.org/1999/02/22-rdf-syntax-ns# xmlns:rdfs=http://www.w3.org/2000/01/rdf-schema# xmlns:owl=http://www.w3.org/2002/07/owl# xmlns:nejiterms=http://monodzukurilod.org/neji/terms# version=XHTML+RDFa 1.0 xml:lang=ja><head profile=http://www.w3.org/1999/xhtml/vocab><meta http-equiv=Content-Type content=text/html; charset=UTF-8>");
    	out.println("<title>恵那バスLOD</title>");
    	//out.println("<link rel=stylesheet href=/css/table.css>");
    	out.println("<script type=text/javascript src=http://maps.google.com/maps/api/js?sensor=false></script></head>");
    	//out.println("<body><h1 align=center>http://localhost:8080/Resource"+ subject + "</h1>");
    	out.println("<body>");
		out.println("<h1>恵那市　観光情報</h1>");

		while (it.hasNext()) {
			Spot spot = it.next();
			System.out.println("------------------------------------------");
			System.out.println(spot.getLabel());
			System.out.println(spot.getComment());
			System.out.println(spot.getAddress());
			System.out.println(spot.getBus_stop());
			System.out.println("------------------------------------------");
			if (spot.getLabel() != null) {
				//out.println("<table width=60% align=center border=1 style='width=100px;'><tbody><tr><th>Property</th></tr>");
				out.println("<table border=0><tr><td border=0><font size=5><b>"+ spot.getLabel() + "</b></font></h2></td></tr>");
				if(spot.getComment()!=null)
					out.println("<tr><td>"+spot.getComment()+"</td></tr>");
				if(spot.getAddress()!=null)
					out.println("<tr><td>所在地　：　"+spot.getAddress()+"</td></tr>");
				out.println("<tr><td><a href="+spot.getBus_stop()+">最寄りのバス停</a></td></tr></table>");
				out.println("<br/>");
			}
		}
		out.println("</body></html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
