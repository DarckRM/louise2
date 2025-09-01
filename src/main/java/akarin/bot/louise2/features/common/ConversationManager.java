package akarin.bot.louise2.features.common;

import akarin.bot.louise2.domain.onebot.event.PostEvent;
import akarin.bot.louise2.domain.onebot.event.message.GroupMessageEvent;
import akarin.bot.louise2.domain.onebot.event.message.MessageEvent;
import akarin.bot.louise2.domain.onebot.event.message.PrivateMessageEvent;
import akarin.bot.louise2.domain.onebot.event.meta.MetaEvent;
import akarin.bot.louise2.domain.onebot.event.notification.NotificationEvent;
import akarin.bot.louise2.domain.onebot.event.request.RequestEvent;
import akarin.bot.louise2.domain.onebot.model.message.ArrayMessage;
import akarin.bot.louise2.function.InteractFunction;
import akarin.bot.louise2.function.InteractFunctionWrapper;
import akarin.bot.louise2.service.OnebotService;
import akarin.bot.louise2.utils.LouiseThreadPool;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author akarin
 * @version 1.0
 * @description
 * @date 2025/6/10 09:34
 */
@Component
@Slf4j
public class ConversationManager {

    @Resource
    OnebotService onebotService;

    @Resource
    LouiseThreadPool pool;

    public static final Long ALL_USER = -1L;

    private static final Long INTERVAL = 1L;

    private static final Long DEFAULT_TIMEOUT = 3000L;

    private final Map<Long, Map<InteractFunctionWrapper<?>, Long>> waitingMap = new HashMap<>();

    @PostConstruct
    public void init() {
        pool.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    dropExpiredFunction();
                    TimeUnit.SECONDS.sleep(INTERVAL);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    log.error("等待管理器发生错误！", e);
                }
            }
        });
    }

    private void dropExpiredFunction() {
        for (Map.Entry<Long, Map<InteractFunctionWrapper<?>, Long>> entry : waitingMap.entrySet()) {
            for (Map.Entry<InteractFunctionWrapper<?>, Long> interactEntry : entry.getValue().entrySet()) {
                if (!interactEntry.getKey().isActive())
                    continue;

                interactEntry.setValue(interactEntry.getValue() - (INTERVAL * 1000));
                // 对于超时的交互会话直接执行
                if (interactEntry.getValue() <= 0) {
                    interactEntry.getKey().inactive();
                    interactEntry.getKey().execute(null);
                    interactEntry.getKey().wakeup();
                }
            }

            // 释放不活跃的包装器
            entry.getValue().entrySet().removeIf(k -> !k.getKey().isActive());
        }
    }

    public InteractFunctionWrapper<?> waiting(String prompt, PostEvent event, InteractFunction<?> function) {
        return waiting(prompt, event, function, DEFAULT_TIMEOUT);
    }

    public InteractFunctionWrapper<?> waitUser(String prompt, PostEvent event, Long userId,
                                               InteractFunction<?> function) {
        return waitUser(prompt, event, userId, function, DEFAULT_TIMEOUT);
    }

    public InteractFunctionWrapper<?> waitSender(String prompt, PostEvent event, InteractFunction<?> function) {
        return waitSender(prompt, event, function, DEFAULT_TIMEOUT);
    }

    public InteractFunctionWrapper<?> waitUser(String prompt, PostEvent event, Long userId,
                                               InteractFunction<?> function, Long timeout) {
        prepareWait(prompt, event);
        InteractFunctionWrapper<?> wrapper = new InteractFunctionWrapper<>(function);
        waitingMap.computeIfAbsent(userId, k -> new HashMap<>()).put(wrapper, timeout);
        return functionWaiting(wrapper);
    }

    public InteractFunctionWrapper<?> waitSender(String prompt, PostEvent event, InteractFunction<?> function,
                                                 Long timeout) {
        if (event instanceof MessageEvent messageEvent) {
            prepareWait(prompt, event);
            InteractFunctionWrapper<?> wrapper = new InteractFunctionWrapper<>(function);
            waitingMap.computeIfAbsent(messageEvent.getSender().getUserId(), k -> new HashMap<>()).put(wrapper,
                    timeout);
            return functionWaiting(wrapper);
        } else
            throw new IllegalArgumentException("等待原事件用户交互只能用于消息事件！");
    }

    public InteractFunctionWrapper<?> waiting(String prompt, PostEvent event, InteractFunction<?> function,
                                              Long timeout) {
        prepareWait(prompt, event);
        InteractFunctionWrapper<?> wrapper = new InteractFunctionWrapper<>(function);
        waitingMap.computeIfAbsent(ALL_USER, k -> new HashMap<>()).put(wrapper, timeout);
        return functionWaiting(wrapper);
    }

    private void prepareWait(String prompt, PostEvent event) {
        if (null == prompt || prompt.isEmpty())
            throw new IllegalArgumentException("等待用户输入的提示词不能为空!");
        switch (event) {
            case GroupMessageEvent groupMessageEvent ->
                    onebotService.sendGroupMessage(groupMessageEvent.getGroupId(), new ArrayMessage().text(prompt));
            case PrivateMessageEvent privateMessageEvent ->
                    onebotService.sendPrivateMessage(privateMessageEvent.getUserId(), new ArrayMessage().text(prompt));
            case null, default -> throw new IllegalArgumentException("等待用户输入的提示词只能用于消息事件！");
        }
    }

    private InteractFunctionWrapper<?> functionWaiting(InteractFunctionWrapper<?> wrapper) {
        return wrapper.waiting();
    }

    public Integer receiveEvent(PostEvent event) {
        Long userId = null;
        if (event instanceof MetaEvent)
            return 0;
        if (event instanceof MessageEvent messageEvent)
            userId = messageEvent.getSender().getUserId();
        else if (event instanceof NotificationEvent noticeEvent)
            userId = noticeEvent.getUserId();
        else if (event instanceof RequestEvent requestEvent)
            userId = requestEvent.getUserId();

        if (userId == null && waitingMap.containsKey(ALL_USER))
            userId = ALL_USER;
        if (userId == null)
            return 0;

        Map<InteractFunctionWrapper<?>, Long> wrappers = waitingMap.get(userId);
        if (wrappers == null || wrappers.isEmpty())
            return 0;

        pool.submit(() -> {
            for (Map.Entry<InteractFunctionWrapper<?>, Long> entry : wrappers.entrySet()) {
                if (!entry.getKey().isActive())
                    continue;
                entry.getKey().inactive();
                entry.getKey().execute(event);
                entry.getKey().wakeup();
            }
        });
        return 1;
    }
}
