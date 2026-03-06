package week4.framework.core;
import java.lang.reflect.Method;

public class RouteInfo {
    private final Object controller;
    private final Method method;
    private final String httpMethod;
    private final boolean authRequired;

    public RouteInfo(Object controller, Method method, String httpMethod, boolean authRequired) {
        this.controller = controller;
        this.method = method;
        this.httpMethod = httpMethod;
        this.authRequired = authRequired;
    }

    public Object invoke(Object... args) throws Exception { return method.invoke(controller, args); }
    public String getHttpMethod() { return httpMethod; }
    public boolean isAuthRequired() { return authRequired; }
    public Class<?>[] getParameterTypes() { return method.getParameterTypes(); }
}