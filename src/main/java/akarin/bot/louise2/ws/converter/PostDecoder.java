package akarin.bot.louise2.ws.converter;

import akarin.bot.louise2.domain.common.LouiseContext;
import akarin.bot.louise2.domain.onebot.event.PostEvent;
import akarin.bot.louise2.domain.onebot.event.message.GroupMessageEvent;
import akarin.bot.louise2.domain.onebot.event.message.PrivateMessageEvent;
import akarin.bot.louise2.domain.onebot.event.meta.HeartbeatEvent;
import akarin.bot.louise2.domain.onebot.event.meta.LifecycleEvent;
import akarin.bot.louise2.domain.onebot.event.notification.NotificationEvent;
import akarin.bot.louise2.domain.onebot.event.request.RequestEvent;
import com.alibaba.fastjson2.JSONObject;
import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;
import jakarta.websocket.EndpointConfig;
import org.springframework.stereotype.Component;

/**
 * @author akarin
 * @version 1.0
 * @description 上报解码器
 * @date 2025/2/14 16:05
 */
@Component
public class PostDecoder implements Decoder.Text<PostEvent> {

    @Override
    public void init(EndpointConfig endpointConfig) {
        Text.super.init(endpointConfig);
    }

    @Override
    public void destroy() {
        Text.super.destroy();
    }

    @Override
    public PostEvent decode(String s) throws DecodeException {
      return parseMessage(s);
    }

    private PostEvent parseMessage(String s) {
        JSONObject post = JSONObject.parseObject(s);
        return switch (post.getString("post_type")) {
            case "meta_event" -> switch (post.getString("meta_event_type")) {
                case "heartbeat" -> JSONObject.parseObject(s, HeartbeatEvent.class);
                case "lifecycle" -> JSONObject.parseObject(s, LifecycleEvent.class);
                default -> null;
            };
            case "message" -> switch (post.getString("message_type")) {
                case "private" -> JSONObject.parseObject(s, PrivateMessageEvent.class);
                case "group" -> JSONObject.parseObject(s, GroupMessageEvent.class);
                default -> null;
            };
            case "notice" -> JSONObject.parseObject(s, NotificationEvent.class);
            case "request" -> JSONObject.parseObject(s, RequestEvent.class);
            default -> null;
        };
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }
}
