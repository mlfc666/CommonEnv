package week4.framework.core;

import java.util.HashMap;
import java.util.Map;

public class BeanContainer {
    // 存储所有的单例对象：接口类型 -> 实现类实例
    private static final Map<Class<?>, Object> beans = new HashMap<>();

    public static void register(Class<?> interfaceClass, Object instance) {
        beans.put(interfaceClass, instance);
    }

    public static Object get(Class<?> clazz) {
        return beans.get(clazz);
    }
}