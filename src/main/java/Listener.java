package main.java;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;


public class Listener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println(" [LISTENER] L'application de test vient de démarrer !");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[LISTENER] L'application de test s'arrête.");
    }
}
