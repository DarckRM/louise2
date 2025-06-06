package akarin.bot.louise2.ws;

import akarin.bot.louise2.config.LouiseConfig;
import akarin.bot.louise2.domain.common.Context;
import akarin.bot.louise2.domain.onebot.event.PostEvent;
import akarin.bot.louise2.domain.onebot.event.message.MessageEvent;
import akarin.bot.louise2.domain.onebot.event.meta.MetaEvent;
import akarin.bot.louise2.domain.onebot.event.notification.NotificationEvent;
import akarin.bot.louise2.features.common.FeatureManager;
import akarin.bot.louise2.features.common.FeatureMethodInterface;
import akarin.bot.louise2.service.OnebotService;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author akarin
 * @version 1.0
 * @description WebSocket 服务端
 * @date 2025/2/13 16:15
 */
@Component
@Slf4j
@ServerEndpoint(value = "/onebot/v11", decoders = {PostDecoder.class})
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
        List<FeatureMethodInterface> methods = FeatureManager.peekFeature(event);
        Thread.ofVirtual().start(() -> methods.forEach(m -> {
            List<Object> parameters = new ArrayList<>();
            for (Class<?> signature : m.getParameterSignatures()) {
                // 根据 Method 定义的参数列表注入参数
                if (PostEvent.class.isAssignableFrom(signature))
                    parameters.add(event);
                else if (signature.equals(OnebotService.class))
                    parameters.add(context.getBean(OnebotService.class));
                else if (signature.equals(LouiseConfig.class))
                    parameters.add(context.getBean(LouiseConfig.class));
            }
            m.execute(parameters.toArray());
        }));
    }

    public void handleMetaEvent(MetaEvent event) {
        log.info(event.toString());
    }


    public void handleMessageEvent(MessageEvent event) {
        List<FeatureMethodInterface> methods = FeatureManager.peekFeature(event);
        methods.forEach(m -> {
            List<Object> parameters = new ArrayList<>();
            for (Class<?> signature : m.getParameterSignatures()) {
                // 校验签名类型并注入
                if (signature.equals(NotificationEvent.class))
                    parameters.add(event);
                if (signature.equals(MessageEvent.class))
                    parameters.add(event);
            }
            m.execute(parameters.toArray());
        });
    }
}
