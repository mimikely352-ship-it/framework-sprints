package main.java;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class Listener implements ServletContextListener {

    public static final String SPRING_ROOT = "org.springframework.web.context.WebApplicationContext.ROOT";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("L'application de test vient de dmarrer !");
        
        ServletContext servletContext = sce.getServletContext();
        
        servletContext.setAttribute("springContext", servletContext.getAttribute(SPRING_ROOT));
        
        System.out.println("SpringContext a ete configure avec succes.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println(" L'application de test s'arrete.");
    }
}