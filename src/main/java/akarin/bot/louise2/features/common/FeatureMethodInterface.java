package akarin.bot.louise2.features.common;

import akarin.bot.louise2.domain.onebot.event.PostEvent;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author akarin
 * @version 1.0
 * @description 功能方法接口
 * @date 2025/6/4 15:35
 */
public interface FeatureMethodInterface {

    String getMethodName();
    FeatureMethodInterface setMethodName(String name);

    void recordInvoke(Long userId, Long time);

    Long recentInvoke(Long userId);

    Long getCooldown();

    FeatureMethodInterface setCooldown(Long cooldown);

    FeatureInterface getFeatureInterface();

    Method getMethod();

    List<Class<?>> getParameterSignatures();

    void execute(Object... args);

    default boolean permission(PostEvent event) {
        return true;
    }

    default boolean cooldown(PostEvent event) {
        return true;
    }

}
