package utils;

import java.lang.reflect.Method;

public class Reflect {
    public static Object invoke(Object obj, String methodName, Object[] args) {
        try {
            Class<?>[] clazzes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                clazzes[i] = args[i].getClass();
            }
            Method m = obj.getClass().getMethod(methodName, clazzes);
            Object o = m.invoke(obj, args);
            return o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
