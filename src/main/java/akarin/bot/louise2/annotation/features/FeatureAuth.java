package akarin.bot.louise2.annotation.features;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author akarin
 * @version 1.0
 * @description 功能校验注解
 * @date 2025/6/12 11:30
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FeatureAuth {

    String code() default "";

    String name() default "";

    // 白名单
    String[] allowed() default {};

    // 黑名单
    String[] denied() default {};

    String cooldown() default "0";

}
