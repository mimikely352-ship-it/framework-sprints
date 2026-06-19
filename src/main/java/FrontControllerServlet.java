package main.java;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontControllerServlet extends HttpServlet {

    private List<String> listcontroller;

    public void init() throws ServletException {
        try {
            String packageToScan = this.getInitParameter("controllerParam");
            
            if (packageToScan == null || packageToScan.trim().isEmpty()) {
                throw new ServletException("Le paramètre 'packageControllers' est manquant dans le web.xml");
            }

            this.listcontroller = Utilitaire.scanControllers(packageToScan, "main.annotation.Controller");
            
            System.out.println("Scan termine. Controleurs charges : " + listcontroller);
        } catch (Exception e) {
            throw new ServletException("Échec de l'initialisation du FrontController", e);
        }
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String url = request.getRequestURI();
        // response.getWriter().println("URL: " + url);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<html><body>");
        response.getWriter().println("<h1>URL interceptee : " + url + "</h1>");
        response.getWriter().println("<h2>Controleurs detectes :</h2>");
        for (String c : listcontroller) {
            response.getWriter().println("<p>" + c + "</p>");
        }
        
        response.getWriter().println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }
}