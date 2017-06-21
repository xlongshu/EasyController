package longshu.easycontroller.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ActionKey 控制器的映射
 * 以/开头则为完整的请求路径,否者加上为 controllerKey/actionKey
 *
 * @author LongShu 2017/05/09
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ActionKey {

    /**
     * Action URI
     */
    String value() default "";

    /**
     * Supported method
     * 默认都支持,不支持时返回 405 Method Not Allowed
     *
     * @see HttpMethod
     */
    HttpMethod[] methods() default {};

}
