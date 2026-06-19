package main.java;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Utilitaire {
    public static List<String> scanControllers(String packageToScan, String annotationName) throws Exception {
        List<String> annotatedClasses = new ArrayList<>();
        String packagePath = packageToScan.replace('.', '/');
        
        URL resource = Utilitaire.class.getClassLoader().getResource(packagePath);
        
        if (resource == null) {
            return annotatedClasses;
        }
    
        File directory = new File(resource.getFile());
        if (directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.getName().endsWith(".class")) {
                    String className = packageToScan + "." + file.getName().substring(0, file.getName().length() - 6);
                    Class<?> clazz = Class.forName(className);
                    
                    if (isAnnotationPresent(clazz, annotationName)) {
                        annotatedClasses.add(clazz.getName());
                    }
                }
            }
        }
        return annotatedClasses;
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