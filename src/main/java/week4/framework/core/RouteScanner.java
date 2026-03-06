package week4.framework.core;

import week4.framework.annotations.*;
import week4.framework.models.RouteInfo;
import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class RouteScanner {

    public static void scan(String packageName) throws Exception {
        String path = packageName.replace('.', '/');
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = loader.getResources(path);

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if (resource.getProtocol().equals("file")) {
                // 处理本地开发环境
                scanFile(new File(resource.getFile()), packageName);
            } else if (resource.getProtocol().equals("jar")) {
                // 处理 JAR 包
                scanJar(resource, path);
            }
        }
    }

    private static void scanFile(File directory, String packageName) throws Exception {
        if (directory == null || !directory.exists()) return;
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                // 递归扫描子包
                scanFile(file, packageName + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                loadClass(packageName + "." + file.getName().replace(".class", ""));
            }
        }
    }

    private static void scanJar(URL resource, String path) throws Exception {
        JarURLConnection conn = (JarURLConnection) resource.openConnection();
        try (JarFile jar = conn.getJarFile()) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                String entryName = entries.nextElement().getName();
                if (entryName.startsWith(path) && entryName.endsWith(".class")) {
                    String className = entryName.replace('/', '.').replace(".class", "");
                    loadClass(className);
                }
            }
        }
    }

    private static void loadClass(String className) throws Exception {
        Class<?> cls = Class.forName(className);
        if (cls.isAnnotationPresent(RestController.class)) {
            // 每次扫描只创建一个单例 Controller 实例
            Object instance = cls.getDeclaredConstructor().newInstance();
            injectDependencies(instance);
            registerMethods(instance);
        }
    }

    private static void injectDependencies(Object instance) throws Exception {
        for (java.lang.reflect.Field field : instance.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                Object dependency = BeanContainer.get(field.getType());
                if (dependency == null) {
                    throw new RuntimeException("Dependency missing for: " + field.getType().getName());
                }
                field.setAccessible(true);
                field.set(instance, dependency);
            }
        }
    }

    private static void registerMethods(Object instance) {
        boolean classAuth = instance.getClass().isAnnotationPresent(RequiresAuth.class);
        for (java.lang.reflect.Method m : instance.getClass().getDeclaredMethods()) {
            boolean auth = classAuth || m.isAnnotationPresent(RequiresAuth.class);
            if (m.isAnnotationPresent(GetMapping.class)) {
                DispatcherHandler.registerRoute(m.getAnnotation(GetMapping.class).value(),
                        new RouteInfo(instance, m, "GET", auth));
            } else if (m.isAnnotationPresent(PostMapping.class)) {
                DispatcherHandler.registerRoute(m.getAnnotation(PostMapping.class).value(),
                        new RouteInfo(instance, m, "POST", auth));
            }
        }
    }
}