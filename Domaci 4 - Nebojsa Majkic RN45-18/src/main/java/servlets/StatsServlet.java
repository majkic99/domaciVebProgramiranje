package servlets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.Database;
@WebServlet(name = "statsServlet" , value = "/stats")
public class StatsServlet extends HttpServlet {

	private Map<String, Map<String, Integer>> database;
	private String password;
    public void init(){
        database = Database.getDatabaseInstance();
        try {
			Scanner scanner = new Scanner(new File("E:\\GitRepos\\domaciVebProgramiranje\\Domaci 4 - Nebojsa Majkic RN45-18\\src\\main\\resources\\password.txt"));
			password = scanner.nextLine();
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    	response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        try {
        	if (!(request.getParameter("password").equals(password))){
        		out.println("<h1>" +"Pogresna sifra" + "</h1>" + request.getParameter("password"));
                return;
        	}
        }catch(Exception e) {
        	out.println("<h1>" +"Pogresna sifra" + "</h1>");
            return;
        }
        
        out.println("<html><body><form method=\"POST\" action = \"stats?password="+password+"\">");
        out.println("<br><input type=\"submit\" name\"submit\" value\"Izbrisi\"/></form>");
        out.println("</body></html>");
        
        List<String> listDays = new ArrayList<>();
        listDays.add("Monday");
        listDays.add("Tuesday");
        listDays.add("Wednesday");
        listDays.add("Thursday");
        listDays.add("Friday");
        
        for(String day : listDays) {
        	out.println("<h2>" + day + "</h2>");
            for (String meal : database.get(day).keySet()) {
            	out.println("<h2>" + meal + "</h2>" + " : " + database.get(day).get(meal));
            }
        }
    	
    	
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	response.setContentType("text/html");
        PrintWriter out = response.getWriter();
    	try {
        	if (!(request.getParameter("password").equals(password))){
        		out.println("<h1>" +"Pogresna sifra" + "</h1>" + request.getParameter("password"));
                return;
        	}
        }catch(Exception e) {
        	out.println("<h1>" +"Pogresna sifra" + "</h1>");
            return;
        }
    	reset();
    	System.out.println("here");
    	response.sendRedirect("/Domaci4-NebojsaMajkicRN45-18/stats?password="+password);
    }
    
    private void reset() {
    	List<String> listDays = new ArrayList<>();
        listDays.add("Monday");
        listDays.add("Tuesday");
        listDays.add("Wednesday");
        listDays.add("Thursday");
        listDays.add("Friday");
        
        for(String day : listDays) {
            for (String meal : database.get(day).keySet()) {
            	database.get(day).put(meal, 0);
            }
        }
    	
    	
    	Map<String, String> map = (Map<String, String>) getServletContext().getAttribute("map");
    	for (String id : map.keySet()) {
    		getServletContext().removeAttribute(id);
    	}
    	map.clear();
    	
    }
}
