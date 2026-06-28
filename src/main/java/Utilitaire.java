package main.java;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import main.annotation.UrlMapping;

public class Utilitaire {
    
    public static List<String> scanControllers(String packageToScan, String annotationController) throws Exception {
        List<String> annotatedClasses = new ArrayList<>();
        String packagePath = packageToScan.replace('.', '/');
        
        URL resource = Utilitaire.class.getClassLoader().getResource(packagePath);
        if (resource == null) return annotatedClasses;

        File directory = new File(resource.getFile());
        if (directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.getName().endsWith(".class")) {
                    String className = packageToScan + "." + file.getName().substring(0, file.getName().length() - 6);
                    Class<?> clazz = Class.forName(className);
                    
                    if (isAnnotationPresent(clazz, annotationController)) {
                        annotatedClasses.add(clazz.getName());
                    }
                }
            }
        }
        return annotatedClasses;
    }

    public static HashMap<String, Mapping> scanKeyMapping(String packageToScan, String annotationController) throws Exception {
    HashMap<String, Mapping> urlMap = new HashMap<>();
    String packagePath = packageToScan.replace('.', '/');
    
    URL resource = Utilitaire.class.getClassLoader().getResource(packagePath);
    if (resource == null) return urlMap;

    File directory = new File(resource.getFile());
    if (directory.exists() && directory.isDirectory()) {
        for (File file : directory.listFiles()) {
            if (file.getName().endsWith(".class")) {
                String className = packageToScan + "." + file.getName().substring(0, file.getName().length() - 6);
                Class<?> clazz = Class.forName(className);
                
                if (isAnnotationPresent(clazz, annotationController)) {
                    for (Method method : clazz.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(UrlMapping.class)) {
                            UrlMapping urlMapping = method.getAnnotation(UrlMapping.class);
                            String url = urlMapping.value(); 
                            
                            System.out.println(" Scan de l'URL : " + url + " dans " + clazz.getSimpleName() + " -> " + method.getName());
                             if (urlMap.containsKey(url)) {
                                 Mapping duplicate = urlMap.get(url);
                                 throw new Exception("❌ ERREUR : L'URL '" + url + "' est déjà associée à " 
                                     + duplicate.getClassName() + " -> " + duplicate.getMethod() 
                                     + ". Double emploi interdit avec " + clazz.getSimpleName() + " -> " + method.getName());
                             }
                            
                            Mapping mapping = new Mapping(clazz.getSimpleName(), method.getName());
                            urlMap.put(url, mapping);
                        }
                    }
                }
            }
        }
    }
    return urlMap;
}

    public static boolean isAnnotationPresent(Class<?> clazz, String annotationFullName) {
        for (java.lang.annotation.Annotation annotation : clazz.getAnnotations()) {
            if (annotation.annotationType().getName().equals(annotationFullName)) {
                return true;
            }
        }
        return false;
    }
}