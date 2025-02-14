package akarin.bot.louise2.ws;

import akarin.bot.louise2.domain.onebot.event.PostEvent;
import akarin.bot.louise2.domain.onebot.event.message.GroupMessageEvent;
import akarin.bot.louise2.domain.onebot.event.message.MessageEvent;
import akarin.bot.louise2.domain.onebot.event.meta.HeartbeatEvent;
import akarin.bot.louise2.domain.onebot.event.meta.MetaEvent;
import akarin.bot.louise2.ws.converter.PostDecoder;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author akarin
 * @version 1.0
 * @description TODO))
 * @date 2025/2/13 16:15
 */
@Component
@Slf4j
@ServerEndpoint(value = "/ws/onebot", decoders = {PostDecoder.class})
public class WebsocketChannel implements ApplicationContextAware {
    private static ApplicationContext context;
    private Session session;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext context) throws BeansException {
        WebsocketChannel.context = context;
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        log.info("[websocket] 连接建立: id = {}", this.session.getId());
    }

    @OnMessage
    public void onMessage(PostEvent event) {
        if (event instanceof MetaEvent)
            handleMetaEvent((MetaEvent) event);
        else if (event instanceof MessageEvent)
            handleMessageEvent((MessageEvent) event);
    }

    public void handleMetaEvent(MetaEvent event) {
        log.info(event.toString());
    }


    public void handleMessageEvent(MessageEvent event) {
        log.info(event.toString());
    }
}
