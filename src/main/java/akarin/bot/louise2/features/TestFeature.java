package akarin.bot.louise2.features;

import akarin.bot.louise2.annotation.features.LouiseFeature;
import akarin.bot.louise2.annotation.features.OnCommand;
import akarin.bot.louise2.config.LouiseConfig;
import akarin.bot.louise2.domain.onebot.event.message.MessageEvent;
import akarin.bot.louise2.domain.onebot.model.message.ArrayMessage;
import akarin.bot.louise2.features.common.Feature;
import akarin.bot.louise2.features.common.FeatureInterface;
import akarin.bot.louise2.service.OnebotService;
import jakarta.annotation.Resource;

/**
 * @author akarin
 * @version 1.0
 * @description 测试功能
 * @date 2025/6/4 16:03
 */
@LouiseFeature(name = "测试功能")
public class TestFeature extends Feature implements FeatureInterface {

    @Override
    public String getName() {
        return "测试功能";
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

    }

    @OnCommand("!test")
    public void testDependencyInject(MessageEvent message, OnebotService bot) {
        bot.sendPrivateMessage(412543224L, new ArrayMessage().text("测试功能成功！"));
    }

    @OnCommand("!image")
    public void testImageSend(OnebotService bot, LouiseConfig config) {
        bot.sendPrivateMessage(config.getAdminNumber(), new ArrayMessage().text("测试图片发送！")
                .image(config.getCachePath() + "/sample.jpg", "file"));
    }
}
