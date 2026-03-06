package week4.framework.models;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class RouteInfo {
    private final Object controller;
    private final Method method;
    private final String httpMethod;
    private final boolean authRequired;
    private final Parameter[] parameters;

    public RouteInfo(Object controller, Method method, String httpMethod, boolean authRequired) {
        this.controller = controller;
        this.method = method;
        this.httpMethod = httpMethod;
        this.authRequired = authRequired;
        this.parameters = method.getParameters();
    }

    public Object invoke(Object... args) throws Exception { return method.invoke(controller, args); }
    public String getHttpMethod() { return httpMethod; }
    public boolean isAuthRequired() { return authRequired; }
    public Parameter[] getParameters() { return parameters; }
}