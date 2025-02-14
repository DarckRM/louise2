package akarin.bot.louise2.ws;

import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@ServerEndpoint(value = "/ws/onebot")
public class WebsocketChannel implements ApplicationContextAware {
    Logger log = LoggerFactory.getLogger(WebsocketChannel.class);
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
    public void onMessage(String message) {
        System.out.println(message);
    }
}
