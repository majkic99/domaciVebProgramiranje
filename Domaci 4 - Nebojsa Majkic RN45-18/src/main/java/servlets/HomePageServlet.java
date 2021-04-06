package servlets;
import java.io.*;
import java.util.*;
import javax.servlet.http.*;

import domain.Database;

import javax.servlet.annotation.*;

@WebServlet(name = "homePageServlet" , value = "/")
public class HomePageServlet extends HttpServlet {
    private Map<String, Map<String, Integer>> database;
    public void init(){
        database = Database.getDatabaseInstance();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if(getServletContext().getAttribute(request.getSession().getId()) != null){
            out.println("<h1>" +"Zavrsio uspesno" + "</h1>");
            return;
        }
        out.println("<html><body><form method=\"POST\" action = \"homePageServlet\">");
        out.println("<h1>" +"Izaberi rucak" + "</h1>");
        List<String> listDays = new ArrayList<>();
        listDays.add("Monday");
        listDays.add("Tuesday");
        listDays.add("Wednesday");
        listDays.add("Thursday");
        listDays.add("Friday");
        
        for(String day : listDays) {
        	out.println("<h2>" + day + "</h2>");
            out.println("<select name = \""+ day + "\" id=\"" + day + "\">");
            for (String meal : database.get(day).keySet()) {
            	out.println("<option value = \"" + meal + "\" selected>" + meal + "</option>");
            }
            out.println("</select><br>");
        }
        out.println("<br><input type=\"submit\" name\"submit\" value\"Zapamti\"/></form>");
        out.println("</body></html>");
        getServletContext().setAttribute("SvaJela", database);
        
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	List<String> listDays = new ArrayList<>();
        listDays.add("Monday");
        listDays.add("Tuesday");
        listDays.add("Wednesday");
        listDays.add("Thursday");
        listDays.add("Friday");
        
    	getServletContext().setAttribute(request.getSession().getId(),true);
    	for (String day : listDays) {
    		database.get(day).put(request.getParameter(day), database.get(day).get(request.getParameter(day)) + 1);
    	}
    	response.sendRedirect("/Domaci4-NebojsaMajkicRN45-18/");
    }
}
