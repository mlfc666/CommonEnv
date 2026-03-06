package week4.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 作用于参数
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthClaim {
    String value(); // 对应 JWT Payload 中的 Key
}