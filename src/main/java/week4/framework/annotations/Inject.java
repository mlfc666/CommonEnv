package week4.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) // 作用于字段
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {} // 标记需要自动注入的组件