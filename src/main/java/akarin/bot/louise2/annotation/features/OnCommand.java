package akarin.bot.louise2.annotation.features;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author akarin
 * @version 1.0
 * @description 响应命令注解
 * @date 2025/6/4 15:20
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnCommand {
    String name() default "";

    String[] value();
}
