package akarin.bot.louise2.features.common;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author akarin
 * @version 1.0
 * @description 功能方法类
 * @date 2025/6/4 15:57
 */
@Data
@Accessors(chain = true)
@Slf4j
public class FeatureMethod implements FeatureMethodInterface {

    private String methodCode;

    private String methodName;

    private Map<Long, Long> invokeRecord = new HashMap<>();

    private Long cooldown = 1000L;

    private FeatureInterface featureInterface;

    private final List<Class<?>> parameterSignatures = new ArrayList<>();

    private Method method;

    public FeatureMethod(FeatureInterface featureInterface, Method method) {
        this.featureInterface = featureInterface;
        this.method = method;
    }

    @Override
    public void recordInvoke(Long userId, Long time) {
        invokeRecord.put(userId, time);
    }

    @Override
    public Long recentInvoke(Long userId) {
        return invokeRecord.getOrDefault(userId, 0L);
    }

    @Override
    public void execute(Object... args) {
        try {
            method.invoke(featureInterface, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("执行功能方法时发生错误", e);
        }
    }
}
