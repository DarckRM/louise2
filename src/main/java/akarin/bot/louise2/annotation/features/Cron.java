package akarin.bot.louise2.annotation.features;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author akarin
 * @version 1.0
 * @description Cron 表达式注解
 * @date 2025/6/17 16:30
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cron {
    String value();
}
