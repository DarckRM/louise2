package akarin.bot.louise2.features.common;

import akarin.bot.louise2.domain.onebot.event.PostEvent;
import akarin.bot.louise2.domain.onebot.event.message.GroupMessageEvent;
import akarin.bot.louise2.domain.onebot.event.message.MessageEvent;
import akarin.bot.louise2.domain.onebot.event.message.PrivateMessageEvent;
import akarin.bot.louise2.domain.onebot.model.message.ArrayMessage;
import akarin.bot.louise2.function.InteractFunction;
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
public class WaitingManager {

    @Resource
    OnebotService onebotService;

    @Resource
    LouiseThreadPool pool;

    public static final Long ALL_USER = -1L;

    private static final Long INTERVAL = 1L;

    private final Map<Long, Map<InteractFunction<?>, Long>> waitingMap = new HashMap<>();

    @PostConstruct
    public void init() {
        pool.submit(() -> {
            while (true) {
                try {
                    dropExpiredFunction();
                    TimeUnit.SECONDS.sleep(INTERVAL);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    log.error("等待管理器发生错误！", e);
                }
            }
        });
    }

    private void dropExpiredFunction() {
        for (Map.Entry<Long, Map<InteractFunction<?>, Long>> entry : waitingMap.entrySet()) {
            for (Map.Entry<InteractFunction<?>, Long> interactEntry : entry.getValue().entrySet()) {
                interactEntry.setValue(interactEntry.getValue() - (INTERVAL * 1000));
                // 对于超时的交互会话直接执行
                if (interactEntry.getValue() <= 0) {
                    Thread.ofVirtual().start(() -> interactEntry.getKey().execute(null));
                    entry.getValue().remove(interactEntry.getKey());
                }
            }
        }
    }

    public void waiting(String prompt, PostEvent event, InteractFunction<?> function) {
        waiting(prompt, event, function, 0L);
    }

    public void waitingSomeone(String prompt, PostEvent event, Long userId, InteractFunction<?> function) {
        waitingSomeone(prompt, event, userId, function, 0L);
    }

    public void waitingSender(String prompt, PostEvent event, InteractFunction<?> function) {
        waitingSender(prompt, event, function, 0L);
    }

    public void waitingSomeone(String prompt, PostEvent event, Long userId, InteractFunction<?> function,
                               Long timeout) {
        prepareWait(prompt, event);
        waitingMap.computeIfAbsent(userId, k -> new HashMap<>()).put(function, timeout);
    }

    public void waitingSender(String prompt, PostEvent event, InteractFunction<?> function, Long timeout) {
        if (event instanceof MessageEvent messageEvent) {
            prepareWait(prompt, event);
            waitingMap.computeIfAbsent(messageEvent.getSender().getUserId(), k -> new HashMap<>()).put(function,
                    timeout);
        } else
            throw new IllegalArgumentException("等待原事件用户交互只能用于消息事件！");
    }

    public void waiting(String prompt, PostEvent event, InteractFunction<?> function,
                        Long timeout) {
        prepareWait(prompt, event);
        waitingMap.computeIfAbsent(ALL_USER, k -> new HashMap<>()).put(function, timeout);
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

    public void receiveEvent(PostEvent event) {
        // TODO)) 根据上报事件类型尝试获取等待的用户 QQ
    }
}
