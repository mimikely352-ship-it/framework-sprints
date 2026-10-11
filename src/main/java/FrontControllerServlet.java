package main.java;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList; 
import java.util.List;
import java.util.Map;
import java.util.HashMap; 

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontControllerServlet extends HttpServlet {

    private List<String> listcontroller;
    private HashMap<URLMethod, Mapping> urlMappingStructure; 
    private String prefix;
    private String suffix;

    @Override
    public void init() throws ServletException {
        try {
            String packageToScan = this.getInitParameter("controllerParam");
            this.prefix = this.getInitParameter("viewPrefix");
            this.suffix = this.getInitParameter("viewSuffix");
            
            if (packageToScan == null || packageToScan.trim().isEmpty()) {
                throw new ServletException("Le paramètre 'packageControllers' est manquant dans le web.xml");
            }

            if (this.prefix == null) {
                this.prefix = "/";
            }
            if (this.suffix == null) {
                this.suffix = ".jsp";
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

        String contextPath = request.getContextPath();
        String urlDemandee = url.substring(contextPath.length());

        URLMethod cleRecherche = new URLMethod(urlDemandee, httpMethod);
        Mapping match = urlMappingStructure.get(cleRecherche);

        if (match != null) {
            try {
                String fullClassName = "main.java.Controller." + match.getClassName();
                Class<?> clazz = Class.forName(fullClassName);
                Object instanceControleur = clazz.getDeclaredConstructor().newInstance();
                
                Method methodeAInvoquer = null;
                for (Method m : clazz.getDeclaredMethods()) {
                    if (m.getName().equals(match.getMethod())) {
                        methodeAInvoquer = m;
                        break;
                    }
                }

                if (methodeAInvoquer == null) {
                    throw new NoSuchMethodException("Méthode " + match.getMethod() + " introuvable dans " + fullClassName);
                }

                Parameter[] param = methodeAInvoquer.getParameters();
                Object[] objet = new Object[param.length];
                List<String> dejaPris = new java.util.ArrayList<>();

                for (int i = 0; i < param.length; i++) {
                    Class<?> type = param[i].getType();
                    String nomParametre = param[i].getName(); 

                    boolean estTypeSimple = type.isPrimitive() || type == String.class || type == Integer.class || type == Double.class || type == Boolean.class;
                    boolean estTypeComplexe = java.util.List.class.isAssignableFrom(type) || type == int[].class || type == String[].class;

                    if (!estTypeComplexe && !estTypeSimple) {
                        try {
                            Object instance = type.getDeclaredConstructor().newInstance();
                            Field[] fields = type.getDeclaredFields();
                            
                            for (Field field : fields) {
                                field.setAccessible(true);
                                String nomChamp = field.getName();
                                
                                String[] values = request.getParameterValues(nomParametre + "." + nomChamp);
                                if (values == null || values.length == 0) {
                                    values = request.getParameterValues(nomParametre + "_" + nomChamp);
                                }
                                if (values == null || values.length == 0) {
                                    values = request.getParameterValues(nomChamp);
                                }
                                
                                if (values == null || values.length == 0) continue;
                                
                                String val = values[0];
                                Class<?> fieldType = field.getType();
                                
                                if (fieldType == int.class || fieldType == Integer.class) {
                                    field.set(instance, Integer.parseInt(val));
                                } else if (fieldType == double.class || fieldType == Double.class) {
                                    field.set(instance, Double.parseDouble(val));
                                } else if (fieldType == boolean.class || fieldType == Boolean.class) {
                                    field.set(instance, Boolean.parseBoolean(val));
                                } else if (fieldType == String.class) {
                                    field.set(instance, val);
                                }
                            }
                            objet[i] = instance; 

                        } catch (Exception e) {
                            objet[i] = null;
                        }
                    } 
                
                    else {
                        Class<?> typeElement = String.class;

                        if (type == int.class || type == Integer.class) {
                            objet[i] = 0;
                        } else if (type == double.class || type == Double.class) {
                            objet[i] = 0.0;
                        } else if (type == boolean.class || type == Boolean.class) {
                            objet[i] = false;
                        } else if (type == int[].class) {
                            objet[i] = new int[0];
                        } else if (type == String[].class) {
                            objet[i] = new String[0];
                        } else if (java.util.List.class.isAssignableFrom(type)) {
                            objet[i] = new java.util.ArrayList<>();
                            
                            java.lang.reflect.Type genericType = param[i].getParameterizedType();
                            if (genericType instanceof java.lang.reflect.ParameterizedType) {
                                java.lang.reflect.ParameterizedType pt = (java.lang.reflect.ParameterizedType) genericType;
                                java.lang.reflect.Type[] args = pt.getActualTypeArguments();
                                if (args.length > 0 && args[0] instanceof Class) {
                                    typeElement = (Class<?>) args[0]; 
                                }
                            }                    
                        } else {
                            objet[i] = null;
                        }

                        String[] values = null;
                        String matchedKey = null;

                        values = request.getParameterValues(nomParametre);
                        if (values != null && values.length > 0) {
                            matchedKey = nomParametre;
                        } else {
                            java.util.Enumeration<String> allParams = request.getParameterNames();
                            while (allParams.hasMoreElements()) {
                                String pName = allParams.nextElement();
                                if (dejaPris.contains(pName)) continue;

                                String[] tempValues = request.getParameterValues(pName);
                                if (tempValues != null && tempValues.length > 0) {
                                    values = tempValues;
                                    matchedKey = pName;
                                    break;
                                }
                            }
                        }

                        if (matchedKey != null) {
                            dejaPris.add(matchedKey);
                        }

                        if (values != null && values.length > 0) {
                            try {
                                if (java.util.List.class.isAssignableFrom(type)) {
                                    java.util.List<Object> list = new java.util.ArrayList<>();
                                    for (String v : values) {
                                        if (v == null || v.trim().isEmpty()) continue;
                                        
                                        if (typeElement == Integer.class || typeElement == int.class) {
                                            list.add(Integer.parseInt(v));
                                        } else if (typeElement == Double.class || typeElement == double.class) {
                                            list.add(Double.parseDouble(v));
                                        } else if (typeElement == Boolean.class || typeElement == boolean.class) {
                                            list.add(Boolean.parseBoolean(v));
                                        } else {
                                            list.add(v);
                                        }
                                    }
                                    objet[i] = list;
                                } 
                                else if (type == int[].class) {
                                    int[] resultat = new int[values.length];
                                    for (int k = 0; k < values.length; k++) {
                                        resultat[k] = Integer.parseInt(values[k]);
                                    }
                                    objet[i] = resultat;
                                } 
                                else if (type == String[].class) {
                                    objet[i] = values;
                                } 
                                else {
                                    String val = values[0];
                                    if (!val.trim().isEmpty()) {
                                        if (type == int.class || type == Integer.class) {
                                            objet[i] = Integer.parseInt(val);
                                        } else if (type == double.class || type == Double.class) {
                                            objet[i] = Double.parseDouble(val);
                                        } else if (type == boolean.class || type == Boolean.class) {
                                            objet[i] = Boolean.parseBoolean(val);
                                        } else if (type == String.class) {
                                            objet[i] = val;
                                        }
                                    }
                                }
                            } catch (Exception ignored) {}
                        }
                    }
                }
                
                Object resultat = methodeAInvoquer.invoke(instanceControleur, objet);

                if (methodeAInvoquer.isAnnotationPresent(main.annotation.WebApi.class)) {
                    response.setContentType("application/json; charset=UTF-8");
                    
                    String jsonOutput = "";
                    if (resultat != null) {
                        if (resultat instanceof ModelAndView) {
                            resultat = ((ModelAndView) resultat).getAttribute();
                        }
                        jsonOutput = new com.google.gson.Gson().toJson(resultat);
                    } else {
                        jsonOutput = "{}";
                    }
                    
                    response.getWriter().println(jsonOutput);
                    return; 
                }

                if (resultat instanceof ModelAndView) {
                    ModelAndView mv = (ModelAndView) resultat;
                    
                    if (mv.getAttribute() != null) {
                        for (Map.Entry<String, Object> entry : mv.getAttribute().entrySet()) {
                            request.setAttribute(entry.getKey(), entry.getValue());
                        }
                    }
                    
                    String cheminCompletJsp = this.prefix + mv.getView() + this.suffix;               
                    request.getRequestDispatcher(cheminCompletJsp).forward(request, response);
                    return; 
                }

                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().println("<html><body>");
                response.getWriter().println("<h1>URL interceptee : " + url + " (" + httpMethod + ")</h1>");
                response.getWriter().println("<h2>Resultat : " + resultat + "</h2>");
                response.getWriter().println("</body></html>");
               
            } catch (Exception e) {
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().println("<h2 style='color: red;'>Erreur lors de l'execution de la methode :</h2>");
                response.getWriter().println("<pre>");
                e.printStackTrace(response.getWriter());
                response.getWriter().println("</pre>");
            }
            
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h1>URL interceptee : " + url + " (" + httpMethod + ")</h1>");
            response.getWriter().println("<h2>Controleurs detectes :</h2>");
            for (String c : listcontroller) {
                response.getWriter().println("<p>" + c + "</p>");
            }
            response.getWriter().println("<h3>L'URL " + urlDemandee + " [" + httpMethod + "] n'existe pas. Les URLs disponibles sont :</h3>");
            
            for (URLMethod u : urlMappingStructure.keySet()) {
                response.getWriter().println("<p><b>" + u.getUrl() + " [" + u.getMethod() + "]</b></p>");
            }
            response.getWriter().println("</body></html>");
        }
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