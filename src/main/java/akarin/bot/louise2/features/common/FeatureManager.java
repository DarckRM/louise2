package akarin.bot.louise2.features.common;

import akarin.bot.louise2.domain.onebot.event.PostEvent;
import akarin.bot.louise2.domain.onebot.event.message.MessageEvent;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author akarin
 * @version 1.0
 * @description 功能管理器
 * @date 2025/6/4 16:05
 */
@Component
@Data
public class FeatureManager {

    private static final Map<String, FeatureInterface> featureMap = new HashMap<>();

    private FeatureManager() {
    }

    public static void registerFeature(FeatureInterface feature) {
        featureMap.put(feature.getName(), feature);
    }

    public static void unregisterFeature(FeatureInterface feature) {
        featureMap.remove(feature.getName());
    }

    public static List<FeatureMethodInterface> peekFeature(PostEvent event) {
        List<FeatureMethodInterface> handleMethods = new ArrayList<>();

        for (Map.Entry<String, FeatureInterface> entry : featureMap.entrySet()) {
            FeatureInterface feature = entry.getValue();

            if (event instanceof MessageEvent messageEvent) {
                String message = messageEvent.getRawMessage();
                handleMethods.addAll(feature.getCommandMethods(message));
                handleMethods.addAll(feature.getMessageMethods(message));
            } else {
                handleMethods.addAll(feature.getEventMethods(event));
            }
        }

        return handleMethods;
    }

}
