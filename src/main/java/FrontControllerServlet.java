package main.java;

import java.io.IOException;
import java.lang.reflect.Method; // 🟢 Nécessaire pour invoquer la méthode
import java.util.ArrayList; 
import java.util.List;
import java.util.HashMap; 

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontControllerServlet extends HttpServlet {

    private List<String> listcontroller;
    private HashMap<URLMethod, Mapping> urlMappingStructure; 

    @Override
    public void init() throws ServletException {
        try {
            String packageToScan = this.getInitParameter("controllerParam");
            String prefix=this.getInitParameter("viewPrefix");
            String suffix=this.getInitParameter("viewSuffix");
            
            if (packageToScan == null || packageToScan.trim().isEmpty()) {
                throw new ServletException("Le paramètre 'packageControllers' est manquant dans le web.xml");
            }

            if(prefix==null){
                prefix="/";
            }
            if(suffix==null){
                suffix=".jsp";
            }
            this.listcontroller = new ArrayList<>();
            this.urlMappingStructure = Utilitaire.scanKeyMapping(packageToScan, "main.annotation.Controller", this.listcontroller);
            
            System.out.println("Scan termine. Controleurs charges : " + listcontroller);
            
        } catch (Exception e) {
            throw new ServletException("Échec de l'initialisation du FrontController", e);
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String url = request.getRequestURI();
        String httpMethod = request.getMethod(); 
        
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<html><body>");
        response.getWriter().println("<h1>URL interceptee : " + url + " (" + httpMethod + ")</h1>");
        
        response.getWriter().println("<h2>Controleurs detectes :</h2>");
        for (String c : listcontroller) {
            response.getWriter().println("<p>" + c + "</p>");
        }
        
        String contextPath = request.getContextPath();
        String urlDemandee = url.substring(contextPath.length());

        URLMethod cleRecherche = new URLMethod(urlDemandee, httpMethod);
        Mapping match = urlMappingStructure.get(cleRecherche);

        if (match != null) {
            response.getWriter().println("<h2>Methode associee :</h2>");
            response.getWriter().println("<p><b>" + match.getClassName() + "</b> -> " + match.getMethod() + "</p>");
            
            try {
                String fullClassName = "main.java.Controller." + match.getClassName();
                Class<?> clazz = Class.forName(fullClassName);
                
                Object instanceControleur = clazz.getDeclaredConstructor().newInstance();
                
                Method methodeAInvoquer = clazz.getDeclaredMethod(match.getMethod());
                
                Object resultat = methodeAInvoquer.invoke(instanceControleur);
                
                response.getWriter().println("<h2>Resultat de l'execution :</h2>");
                response.getWriter().println("<p>" + resultat + "</p>");
                
            } catch (Exception e) {
                response.getWriter().println("<h2 style='color: red;'>Erreur lors de l'execution de la methode :</h2>");
                response.getWriter().println("<pre>");
                e.printStackTrace(response.getWriter());
                response.getWriter().println("</pre>");
            }
            
        } else {
            response.getWriter().println("<h3>L'URL " + urlDemandee + " [" + httpMethod + "] n'existe pas. Les URLs disponibles sont : \n</h3>");
            
            for (URLMethod u : urlMappingStructure.keySet()) {
                response.getWriter().println("<p><b>" + u.getUrl() + " [" + u.getMethod() + "]\n</b></p>");
            }
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