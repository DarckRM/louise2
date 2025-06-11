package akarin.bot.louise2.annotation.features;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author akarin
 * @version 1.0
 * @description 等待函数式注解
 * @date 2025/6/6 17:42
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LouiseWaiting {

    String expire() default "";

}
