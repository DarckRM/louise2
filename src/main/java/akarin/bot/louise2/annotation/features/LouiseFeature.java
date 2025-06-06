package akarin.bot.louise2.annotation.features;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author akarin
 * @version 1.0
 * @description 功能注解
 * @date 2025/6/4 15:09
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LouiseFeature {
    String name() default "";
}
