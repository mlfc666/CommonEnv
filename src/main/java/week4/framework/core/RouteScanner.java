package week4.framework.core;

import week4.framework.annotations.*;
import week4.framework.models.RouteInfo;

import java.io.File;
import java.net.URL;
import java.util.*;

public class RouteScanner {
    public static void scan(String packageName) throws Exception {
        String path = packageName.replace('.', '/');
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL resource = loader.getResource(path);
        if (resource == null) return;

        File directory = new File(resource.getFile());
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().replace(".class", "");
                Class<?> cls = Class.forName(className);
                if (cls.isAnnotationPresent(RestController.class)) {
                    Object instance = cls.getDeclaredConstructor().newInstance();
                    registerMethods(instance); // 自动识别并注册方法
                }
            }
        }
    }

    private static void registerMethods(Object bean) {
        boolean classAuth = bean.getClass().isAnnotationPresent(RequiresAuth.class); // 检查类注解
        for (java.lang.reflect.Method m : bean.getClass().getDeclaredMethods()) {
            boolean auth = classAuth || m.isAnnotationPresent(RequiresAuth.class); // 方法注解优先级更高
            if (m.isAnnotationPresent(GetMapping.class)) {
                DispatcherHandler.registerRoute(m.getAnnotation(GetMapping.class).value(),
                        new RouteInfo(bean, m, "GET", auth));
            } else if (m.isAnnotationPresent(PostMapping.class)) {
                DispatcherHandler.registerRoute(m.getAnnotation(PostMapping.class).value(),
                        new RouteInfo(bean, m, "POST", auth));
            }
        }
    }
}