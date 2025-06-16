package akarin.bot.louise2.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author akarin
 * @version 1.0
 * @description Louise 配置项
 * @date 2025/6/6 16:39
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "louise")
public class LouiseConfig {

    private Long adminNumber;

    private String cachePath;

    private String onebotApi;

    private String token;

    private String logLevel;

}
