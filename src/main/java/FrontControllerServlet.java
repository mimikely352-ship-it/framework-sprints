package main.java;

import java.io.IOException;
import java.util.List;
import java.util.HashMap; // 🟢 Ajouté

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontControllerServlet extends HttpServlet {

    private List<String> listcontroller;
    private HashMap<String, Mapping> urlMappingStructure; 

    public void init() throws ServletException {
        try {
            String packageToScan = this.getInitParameter("controllerParam");
            
            if (packageToScan == null || packageToScan.trim().isEmpty()) {
                throw new ServletException("Le paramètre 'packageControllers' est manquant dans le web.xml");
            }

            this.listcontroller = Utilitaire.scanControllers(packageToScan, "main.annotation.Controller");
            System.out.println("Scan termine. Controleurs charges : " + listcontroller);
            
            this.urlMappingStructure = Utilitaire.scanKeyMapping(packageToScan, "main.annotation.Controller");
            
        } catch (Exception e) {
            throw new ServletException("Échec de l'initialisation du FrontController", e);
        }
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String url = request.getRequestURI();
        
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<html><body>");
        response.getWriter().println("<h1>URL interceptee : " + url + "</h1>");
        
        response.getWriter().println("<h2>Controleurs detectes :</h2>");
        for (String c : listcontroller) {
            response.getWriter().println("<p>" + c + "</p>");
        }
        
        String contextPath = request.getContextPath();
        String urlDemandee = url.substring(contextPath.length());

        Mapping match = urlMappingStructure.get(urlDemandee);

        if (match != null) {
            response.getWriter().println("<h2>Methode associee :</h2>");
            response.getWriter().println("<p><b>" + match.getClassName() + "</b> -> " + match.getMethod() + "</p>");
        } else {
            StringBuilder msg = new StringBuilder();
            response.getWriter().println("<h3>L'URL "+urlDemandee+ " n'existe pas. Les URLs disponibles sont : \n</h3>");
            for (String u : urlMappingStructure.keySet()) {
                response.getWriter().println("<p><b>"+u+ "\n</b></p>");
            }
            throw new ServletException(msg.toString());
        }
        
        response.getWriter().println("</body></html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }
}