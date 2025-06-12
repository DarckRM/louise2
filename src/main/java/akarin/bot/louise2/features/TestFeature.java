package akarin.bot.louise2.features;

import akarin.bot.louise2.annotation.features.FeatureAuth;
import akarin.bot.louise2.annotation.features.LouiseFeature;
import akarin.bot.louise2.annotation.features.OnCommand;
import akarin.bot.louise2.config.LouiseConfig;
import akarin.bot.louise2.domain.common.LouiseContext;
import akarin.bot.louise2.domain.onebot.event.message.GroupMessageEvent;
import akarin.bot.louise2.domain.onebot.event.message.MessageEvent;
import akarin.bot.louise2.domain.onebot.model.message.ArrayMessage;
import akarin.bot.louise2.exception.EventContinueException;
import akarin.bot.louise2.features.common.Feature;
import akarin.bot.louise2.features.common.FeatureInterface;
import akarin.bot.louise2.features.common.Conversation;
import akarin.bot.louise2.function.InteractFunctionWrapper;
import akarin.bot.louise2.service.OnebotService;
import akarin.bot.louise2.utils.HttpClientUtil;
import akarin.bot.louise2.utils.LouiseThreadPool;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author akarin
 * @version 1.0
 * @description 测试功能
 * @date 2025/6/4 16:03
 */
@Slf4j
@LouiseFeature(name = "测试功能", prefix = "!")
public class TestFeature extends Feature implements FeatureInterface {


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

    @FeatureAuth(name = "依赖注入测试", cooldown = "10")
    @OnCommand("test")
    public void testDependencyInject(MessageEvent message, OnebotService bot) {
        bot.sendPrivateMessage(412543224L, new ArrayMessage().text("测试功能成功！"));
    }

    @OnCommand("image")
    public void testImageSend(OnebotService bot, LouiseConfig config) {
        bot.sendPrivateMessage(config.getAdminNumber(), new ArrayMessage().text("测试图片发送！")
                .image(config.getCachePath() + "/sample.jpg", "file"));
    }


    @FeatureAuth(cooldown = "10")
    @OnCommand("yande")
    public void yandeDaily(OnebotService bot, MessageEvent message, LouiseConfig config, LouiseContext context) {
        String[] split = message.getRawMessage().split(" ");

        String type = "day";
        if (split.length > 1)
            type = split[1];

        // 校验参数合法性
        if (!type.equals("day") && !type.equals("week") && !type.equals("month")) {
            context.reply(new ArrayMessage().text(" 功能仅支持参数 day | week | month，请这样 !yande [参数]"));
            return;
        }

        // 如果是在群聊中发起, 进行内容过滤
        String yandeApi = "https://yande.re/post/popular_by_" + type + ".json?limit=5";

        bot.sendMessage(message, new ArrayMessage().text("开始寻找今天的精选图片~"));

        String sync = HttpClientUtil.builder().get(yandeApi).sync();
        if (sync == null || sync.isEmpty()) {
            bot.sendMessage(message, new ArrayMessage().text("今天没有图片呢，请晚些再试吧 ~"));
            return;
        }

        JSONArray array = JSON.parseArray(sync);

        if (array == null || array.isEmpty()) {
            bot.sendMessage(message, new ArrayMessage().text("今天没有图片呢，请晚些再试吧 ~"));
            return;
        }

        for (Object o : array) {
            Thread.ofVirtual().start(() -> {
                JSONObject json = (JSONObject) o;
                String url = json.getString("sample_url");

                String fileName;
                try (Response resp = HttpClientUtil.builder()
                        .addHeader("Accept", MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        .get(url).syncResp()) {
                    if (resp == null || resp.body() == null)
                        return;

                    fileName = config.getCachePath() + "/image/" + json.getString("md5") + ".jpg";
                    try {
                        Files.copy(resp.body().byteStream(), Path.of("/home/akarin" + fileName));
                    } catch (IOException e) {
                        log.error("处理图片文件时异常: ", e);
                    }
                }
                bot.sendMessage(message, new ArrayMessage().text("今日精选图片：").image(fileName, "file"));
            });
        }
    }
}
