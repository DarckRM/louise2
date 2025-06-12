package akarin.bot.louise2.features.common;

import akarin.bot.louise2.domain.features.FeatureInfo;
import akarin.bot.louise2.domain.onebot.event.api.PostEventInterface;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author akarin
 * @version 1.0
 * @description 功能基础类
 * @date 2025/6/4 15:33
 */
@Data
public class Feature implements FeatureInterface {

    private String name;

    private Map<String, List<FeatureMethodInterface>> commandMethods = new HashMap<>();

    private Map<String, List<FeatureMethodInterface>> messageMethods = new HashMap<>();

    private Map<PostEventInterface, List<FeatureMethodInterface>> eventMethods = new HashMap<>();

    private List<FeatureMethodInterface> methods = new ArrayList<>();

    private FeatureInfo featureInfo;

    @Override
    public List<FeatureMethodInterface> getCommandMethods(String command) {
        String[] split = command.split(" ");
        return commandMethods.getOrDefault(split[0], new ArrayList<>());
    }

    @Override
    public List<FeatureMethodInterface> getMessageMethods(String message) {
        List<String> targets = new ArrayList<>();
        for (Map.Entry<String, List<FeatureMethodInterface>> entry : messageMethods.entrySet()) {
            String regex = entry.getKey();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(message);
            while (matcher.find())
                if (matcher.matches())
                    targets.add(entry.getKey());
        }

        List<FeatureMethodInterface> results = new ArrayList<>();
        for (String target : targets)
            results.addAll(messageMethods.getOrDefault(target, new ArrayList<>()));
        return results;
    }

    @Override
    public List<FeatureMethodInterface> getEventMethods(PostEventInterface event) {
        return eventMethods.getOrDefault(event, new ArrayList<>());
    }

    public void addCommandMethod(String command, FeatureMethodInterface method) {
        this.commandMethods.computeIfAbsent(command, k -> new ArrayList<>()).add(method);
    }

    public void addMessageMethod(String message, FeatureMethodInterface method) {
        for (Map.Entry<String, List<FeatureMethodInterface>> entry : messageMethods.entrySet()) {
            String regex = entry.getKey();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(message);
            while (matcher.find())
                if (matcher.matches())
                    this.messageMethods.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).add(method);
        }
    }

    public void addEventMethod(PostEventInterface event, FeatureMethodInterface method) {
        this.eventMethods.computeIfAbsent(event, k -> new ArrayList<>()).add(method);
    }

    @Override
    public boolean permission() {
        return false;
    }

    @Override
    public boolean cooldown() {
        return false;
    }

    @Override
    public void init() {
        // do nothing
    }

    @Override
    public void enable() {
        FeatureManager.registerFeature(this);
    }

    @Override
    public void disable() {
        FeatureManager.unregisterFeature(this);
    }
}
