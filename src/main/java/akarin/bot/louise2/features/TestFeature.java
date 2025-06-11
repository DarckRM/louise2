package akarin.bot.louise2.features;

import akarin.bot.louise2.annotation.features.LouiseFeature;
import akarin.bot.louise2.annotation.features.OnCommand;
import akarin.bot.louise2.config.LouiseConfig;
import akarin.bot.louise2.domain.onebot.event.message.MessageEvent;
import akarin.bot.louise2.domain.onebot.model.message.ArrayMessage;
import akarin.bot.louise2.features.common.Feature;
import akarin.bot.louise2.features.common.FeatureInterface;
import akarin.bot.louise2.features.common.Conversation;
import akarin.bot.louise2.function.InteractFunctionWrapper;
import akarin.bot.louise2.service.OnebotService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author akarin
 * @version 1.0
 * @description 测试功能
 * @date 2025/6/4 16:03
 */
@Slf4j
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

    @OnCommand("!interact")
    public void testInteract(OnebotService bot, MessageEvent message, LouiseConfig config,
                             Conversation conversation) {

        InteractFunctionWrapper<?> wrapperB = conversation.waitingSender("测试交互功能", message, event -> {
            if (event == null) {
                bot.sendPrivateMessage(config.getAdminNumber(), new ArrayMessage().text("超时未回复！"));
                return 0;
            }

            InteractFunctionWrapper<?> wrapperA = conversation
                    .waitingSender("嵌套测试交互功能", message, event2 -> {
                        if (event2 == null) {
                            bot.sendPrivateMessage(config.getAdminNumber(), new ArrayMessage().text("未收到第二层消息!"));
                            return 0;
                        }
                        log.info("这是嵌套第二层: {}", event2);
                        return 1;
                    }, 10000L);

            bot.sendPrivateMessage(config.getAdminNumber(), new ArrayMessage().text("第二层返回结果: " +
                    wrapperA.getResult().toString()));
            return 1;
        });

        bot.sendPrivateMessage(config.getAdminNumber(), new ArrayMessage().text("第一层返回结果: " +
                wrapperB.getResult().toString()));
    }
}
