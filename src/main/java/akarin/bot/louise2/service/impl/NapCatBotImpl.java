package akarin.bot.louise2.service.impl;

import akarin.bot.louise2.config.LouiseConfig;
import akarin.bot.louise2.domain.onebot.model.api.Message;
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

    @Resource
    private LouiseConfig config;

    @Override
    public void sendPrivateMessage(Long userId, Message message) {
        JSONObject body = new JSONObject();
        body.put("user_id", userId);
        body.put("message", message.getMessages());
        HttpClientUtil.builder()
                .addBody(body.toString())
                .post(config.getOnebotApi() + "/send_private_msg")
                .sync();

    }

    @Override
    public void sendGroupMessage(Long groupId, Message message) {
        String sync = HttpClientUtil.builder().post(config.getOnebotApi() + "/send_group_msg")
                .addParam("group_id", groupId.toString())
                .addParam("message", message.toJSON())
                .sync();

        log.info(sync);
    }
}
