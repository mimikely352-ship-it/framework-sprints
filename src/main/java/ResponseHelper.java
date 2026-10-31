package main.java;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseHelper {

    // Erreur 401 : Non authentifié / Session inexistante
    public static void handleUnauthorized(HttpServletRequest request, HttpServletResponse response, boolean isApi) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        if (isApi) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println("{\"error\": \"Unauthorized\", \"status\": 401, \"message\": \"Vous devez être connecté pour accéder à cette ressource.\"}");
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("<h3 style='color: red;'>Erreur 401 : Acces non autorisé. Veuillez vous connecter.</h3>");
        }
    }

    // Erreur 403 : Utilisateur authentifie, mais ne possedant pas le role requis.
    public static void handleForbidden(HttpServletRequest request, HttpServletResponse response, boolean isApi) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        if (isApi) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println("{\"error\": \"Forbidden\", \"status\": 403, \"message\": \"Accès refusé : rôle requis manquant.\"}");
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("<h3 style='color: red;'>Erreur 403 : Accès refusé. Vous ne possédez pas les privilèges requis.</h3>");
        }
    }
}