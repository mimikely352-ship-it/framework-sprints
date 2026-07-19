package main.java;

import java.lang.reflect.Method;

public class Util {
    public static boolean haveParameter(Method methode, Class<?> param) {
        if (methode == null || param == null) {
            return false;
        }
        
        for (Class<?> pType : methode.getParameterTypes()) {
            if (pType.equals(param)) {
                return true;
            }
        }
        return false;
    }
}