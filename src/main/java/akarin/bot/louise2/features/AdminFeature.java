package akarin.bot.louise2.features;

import akarin.bot.louise2.annotation.features.LouiseFeature;
import akarin.bot.louise2.annotation.features.OnCommand;
import akarin.bot.louise2.domain.onebot.event.message.MessageEvent;
import akarin.bot.louise2.domain.onebot.model.message.ArrayMessage;
import akarin.bot.louise2.service.OnebotService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author akarin
 * @version 1.0
 * @description 管理员插件
 * @date 2025/8/27 15:51
 */
@Slf4j
@LouiseFeature(code = "admin-feature", name = "管理员插件", prefix = "$")
public class AdminFeature {


    @OnCommand("report")
    public void report(OnebotService onebot, MessageEvent event) {
        onebot.reply(event, new ArrayMessage().text("当前系统概览\n"));
    }

    @OnCommand("banned")
    public void banned(OnebotService onebot, MessageEvent event) {

    }

}
