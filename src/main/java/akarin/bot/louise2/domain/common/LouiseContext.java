package akarin.bot.louise2.domain.common;

import akarin.bot.louise2.config.LouiseConfig;
import akarin.bot.louise2.config.PluginConfig;
import akarin.bot.louise2.features.common.ConversationManager;
import jakarta.annotation.Resource;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author akarin
 * @version 1.0
 * @description Louise 上下文
 * @date 2025/2/14 16:32
 */
@Data
public class LouiseContext {

    private final List<Long> blackList = new ArrayList<>();

    private final List<Long> whiteList = new ArrayList<>();

    @Resource
    private final LouiseConfig config;

    @Resource
    private final PluginConfig pluginConfig;

    @Resource
    private final ConversationManager conversationManager;

}
