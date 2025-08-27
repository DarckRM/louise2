package akarin.bot.louise2.features.common;

import akarin.bot.louise2.domain.features.FeatureInfo;
import akarin.bot.louise2.domain.onebot.event.api.PostEventInterface;

import java.util.List;
import java.util.Map;

/**
 * @author akarin
 * @version 1.0
 * @description 功能接口
 * @date 2025/6/4 15:28
 */
public interface FeatureInterface {

    String getCode();

    void setCode(String code);

    String getName();

    void setName(String name);

    void setMethods(List<FeatureMethodInterface> methods);

    List<FeatureMethodInterface> getMethods();

    Map<String, List<FeatureMethodInterface>> commandMethods();

    Map<String, List<FeatureMethodInterface>> messageMethods();

    List<FeatureMethodInterface> getCommandMethods(String command);

    void addCommandMethod(String command, FeatureMethodInterface method);

    List<FeatureMethodInterface> getMessageMethods(String message);

    void addMessageMethod(String message, FeatureMethodInterface method);

    List<FeatureMethodInterface> getEventMethods(PostEventInterface event);

    void addEventMethod(PostEventInterface event, FeatureMethodInterface method);

    void setFeatureInfo(FeatureInfo featureInfo);

    FeatureInfo getFeatureInfo();

    boolean permission();

    boolean cooldown();

    void init();

    void enable();

    void disable();

}
