package akarin.bot.louise2.features;

import akarin.bot.louise2.annotation.features.LouiseFeature;
import akarin.bot.louise2.annotation.features.OnCommand;
import akarin.bot.louise2.domain.onebot.event.message.MessageEvent;
import akarin.bot.louise2.features.common.Feature;
import akarin.bot.louise2.features.common.FeatureInterface;

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
    public void testDependencyInject(MessageEvent message, MessageEvent message2) {
        try {
            Thread.sleep(5000);
            System.out.println("ok");
        } catch (InterruptedException ignore) {
        }
    }
}
