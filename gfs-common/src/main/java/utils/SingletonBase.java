package utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhao.wu on 2016/11/18.
 * ref https://blog.csdn.net/WuZuoDingFeng/article/details/53401623
 */
public class SingletonBase {
    private static Map<Class<? extends SingletonBase>, SingletonBase> INSTANCES_MAP = new HashMap<>();

    public synchronized static <E extends SingletonBase> SingletonBase getInstance(Class<E> instanceClass)
            throws Exception {
        if (INSTANCES_MAP.containsKey(instanceClass)) {
            return INSTANCES_MAP.get(instanceClass);
        } else {
            E instance = instanceClass.getConstructor().newInstance();
            INSTANCES_MAP.put(instanceClass, instance);
            return instance;
        }
    }

    protected SingletonBase() {
    }
}