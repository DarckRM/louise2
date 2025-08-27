package akarin.bot.louise2.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author akarin
 * @version 1.0
 * @description 插件配置类
 * @date 2025/8/27 10:03
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "")
public class PluginConfig {

    @Data
    public static class Plugin {
        private Map<String, Feature> features = new HashMap<>();
    }

    @Data
    public static class Feature {
        private Boolean enable = true;
        private String permission;
        private Integer cooldown = 3;
        private List<String> commands = new ArrayList<>();
    }

    private Map<String, Plugin> plugins = new HashMap<>();

}
