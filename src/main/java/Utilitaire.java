package main.java;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import main.annotation.UrlMapping;

public class Utilitaire {

    public static HashMap<URLMethod, Mapping> scanKeyMapping(String packageToScan, String annotationController, List<String> annotatedClasses) throws Exception {
        HashMap<URLMethod, Mapping> urlMap = new HashMap<>();
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
                        
                        annotatedClasses.add(clazz.getName());
                        
                        for (Method method : clazz.getDeclaredMethods()) {
                            if (method.isAnnotationPresent(UrlMapping.class)) {
                                UrlMapping urlMapping = method.getAnnotation(UrlMapping.class);
                                String url = urlMapping.value(); 
                                
                                String httpMethod = urlMapping.method().toUpperCase();
                                URLMethod key = new URLMethod(url, httpMethod);
                                
                                if (urlMap.containsKey(key)) {
                                    Mapping duplicate = urlMap.get(key);
                                    throw new Exception("ERREUR : L'URL '" + url + "' [" + httpMethod + "] est deja associee à " 
                                        + duplicate.getClassName() + " -> " + duplicate.getMethod());
                                }
                                
                                Mapping mapping = new Mapping(clazz.getSimpleName(), method.getName());
                                urlMap.put(key, mapping);
                            }
                        }
                    }
                }
            }
        }
        return urlMap;
    }

    public static boolean isAnnotationPresent(Class<?> clazz, String annotationFullName) {
        for (Annotation annotation : clazz.getAnnotations()) {
            if (annotation.annotationType().getName().equals(annotationFullName)) {
                return true;
            }
        }
        return false;
    }
}