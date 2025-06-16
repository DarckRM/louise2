package akarin.bot.louise2.service.impl;

import akarin.bot.louise2.config.LouiseConfig;
import akarin.bot.louise2.domain.onebot.event.PostEvent;
import akarin.bot.louise2.domain.onebot.event.message.GroupMessageEvent;
import akarin.bot.louise2.domain.onebot.event.message.PrivateMessageEvent;
import akarin.bot.louise2.domain.onebot.model.api.Message;
import akarin.bot.louise2.domain.onebot.model.message.MessageSegmentInterface;
import akarin.bot.louise2.domain.onebot.model.message.Node;
import akarin.bot.louise2.service.OnebotService;
import akarin.bot.louise2.utils.HttpClientUtil;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author akarin
 * @version 1.0
 * @description OnebotV11 协议 NapCat 实现
 * @date 2025/6/6 13:41
 */
@Service
@Slf4j
public class NapCatBotImpl implements OnebotService {

    private static final String MESSAGE_SLOT = "message";

    private static final String MESSAGES_SLOT = "messages";

    @Resource
    private LouiseConfig config;

    private void request(String url, String body) {
        HttpClientUtil.builder()
                .addBody(body)
                .post(url).sync();
    }

    @Override
    public void sendMessage(PostEvent event, Message message) {
        String url = "";
        Long groupId = 0L;
        Long userId = 0L;

        JSONObject body = new JSONObject();
        if (event instanceof GroupMessageEvent groupMessageEvent) {
            url = config.getOnebotApi() + "/send_group_msg";
            groupId = groupMessageEvent.getGroupId();
            body.put(MESSAGE_SLOT, message.toJSON());
        } else if (event instanceof PrivateMessageEvent privateMessageEvent) {
            url = config.getOnebotApi() + "/send_private_msg";
            userId = privateMessageEvent.getUserId();
            body.put(MESSAGE_SLOT, message.toJSON());
        }

        for (MessageSegmentInterface m : message.getMessages())
            if (m instanceof Node) {
                url = config.getOnebotApi() + "/send_forward_msg";
                body.remove(MESSAGE_SLOT);
                body.put(MESSAGES_SLOT, message.toJSON());
                break;
            }

        if (groupId != 0L)
            body.put("group_id", groupId);
        else if (userId != 0L)
            body.put("user_id", userId);
        else
            throw new IllegalArgumentException("必须指定发送消息的群号或 QQ 号");

        request(url, body.toString());
    }

    @Override
    public void sendPrivateMessage(Long userId, Message message) {
        JSONObject body = new JSONObject();
        body.put("user_id", userId);
        body.put(MESSAGE_SLOT, message.toJSON());
        HttpClientUtil.builder()
                .addBody(body.toString())
                .post(config.getOnebotApi() + "/send_private_msg")
                .sync();

    }

    @Override
    public void sendGroupMessage(Long groupId, Message message) {
        String sync = HttpClientUtil.builder().post(config.getOnebotApi() + "/send_group_msg")
                .addParam("group_id", groupId.toString())
                .addParam(MESSAGE_SLOT, message.toJSON().toString())
                .sync();

        log.info(sync);
    }

    @Override
    public void reply(PostEvent event, Message message) {
        sendMessage(event, message);
    }
}
