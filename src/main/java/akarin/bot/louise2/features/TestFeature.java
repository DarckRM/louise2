package akarin.bot.louise2.features;

import akarin.bot.louise2.annotation.features.FeatureAuth;
import akarin.bot.louise2.annotation.features.LouiseFeature;
import akarin.bot.louise2.annotation.features.OnCommand;
import akarin.bot.louise2.config.LouiseConfig;
import akarin.bot.louise2.domain.common.LouiseContext;
import akarin.bot.louise2.domain.onebot.event.message.MessageEvent;
import akarin.bot.louise2.domain.onebot.model.message.ArrayMessage;
import akarin.bot.louise2.domain.onebot.model.message.NodeMessage;
import akarin.bot.louise2.features.common.Conversation;
import akarin.bot.louise2.features.common.Feature;
import akarin.bot.louise2.features.common.FeatureInterface;
import akarin.bot.louise2.function.InteractFunctionWrapper;
import akarin.bot.louise2.service.OnebotService;
import akarin.bot.louise2.utils.HttpClientUtil;
import akarin.bot.louise2.utils.LouiseThreadPool;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import jakarta.websocket.OnMessage;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.http.MediaType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author akarin
 * @version 1.0
 * @description 测试功能
 * @date 2025/6/4 16:03
 */
@Slf4j
@LouiseFeature(code = "test-feature", name = "测试功能", prefix = "!")
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

    @FeatureAuth(code = "conv-test", cooldown = "5")
    @OnCommand("conv")
    public void testConversation(MessageEvent event, Conversation conversation, OnebotService bot) {
        InteractFunctionWrapper<?> wrapper = conversation.waitingSender("进入监听模式", event, reply -> {
            if (reply == null)
                bot.reply(event, new ArrayMessage().text("等待回复超时"));

            if (reply instanceof MessageEvent replyEvent)
                bot.reply(replyEvent, new ArrayMessage().text("收到消息 %s", replyEvent.getRawMessage()));

            return this;
        });
        log.info(wrapper.toString());
    }

    @FeatureAuth(name = "依赖注入测试", cooldown = "10")
    @OnCommand("test")
    public void testDependencyInject(MessageEvent message, OnebotService bot) {
        bot.sendPrivateMessage(412543224L, new ArrayMessage().text("测试功能成功！"));
    }

    @OnCommand("image")
    public void testImageSend(OnebotService bot, LouiseConfig config, MessageEvent event) {
        event.reply(NodeMessage.init(c -> c.text("输入测试").text("第二段文本").image(config.getCachePath() + "/sample" +
                ".jpg")).node(c -> c.text("多节点测试").text("第三段文本").image(config.getCachePath() + "/sample" + ".jpg")));
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
            message.reply(new ArrayMessage().text(" 功能仅支持参数 day | week | month，请这样 !yande [参数]"));
            return;
        }

        // 如果是在群聊中发起, 进行内容过滤
        String yandeApi = "https://yande.re/post/popular_by_" + type + ".json";

        bot.sendMessage(message, new ArrayMessage().text("开始寻找今天的精选图片~"));

        String sync = HttpClientUtil.builder().get(yandeApi).sync();
        if (sync == null || sync.isEmpty()) {
            bot.sendMessage(message, new ArrayMessage().text("今天没有图片呢，请晚些再试吧 ~"));
            return;
        }

        JSONArray array = JSON.parseArray(sync);
        JSONArray minus = new JSONArray();
        minus.addAll(array.subList(0, array.size() > 10 ? 10 : array.size() - 1));

        if (minus.isEmpty()) {
            bot.sendMessage(message, new ArrayMessage().text("今天没有图片呢，请晚些再试吧 ~"));
            return;
        }

        List<String> images = new ArrayList<>();
        for (Object o : minus) {
            JSONObject json = (JSONObject) o;
            String url = json.getString("sample_url");

            String fileName = config.getCachePath() + "/image/" + json.getString("md5") + ".jpg";
            Path path = Path.of("/home/akarin" + fileName);
            if (Files.exists(path)) {
                images.add(fileName);
                continue;
            }

            try (Response resp = HttpClientUtil.builder()
                    .addHeader("Accept", MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    .get(url).syncResp()) {
                if (resp == null || resp.body() == null)
                    continue;
                Files.copy(resp.body().byteStream(), path);
                images.add(fileName);
            } catch (Exception e) {
                log.error("下载图片失异常: ", e);
            }
        }

        while (!images.isEmpty()) {
            NodeMessage header = NodeMessage.init(c -> c.text("今日精选图片: \n"));
            for (int idx = 0; idx < 3; idx++) {
                if (images.isEmpty())
                    break;
                header.node(c -> c.image("file://" + images.removeFirst()));
            }
            bot.sendMessage(message, header);
        }
    }
}
