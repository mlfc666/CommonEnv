package week4.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE}) // 可用于类或方法
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresAuth {} // 标记需要鉴权的接口