package akarin.bot.louise2.service;

import akarin.bot.louise2.domain.onebot.event.PostEvent;
import akarin.bot.louise2.domain.onebot.model.api.Message;

/**
 * @author akarin
 * @version 1.0
 * @description Onebot 协议消息 Service
 * @date 2025/6/6 14:18
 */
public interface OnebotMessageService {

    /**
     * @param event   指定回应的事件
     * @param message 消息
     * @author akarin
     * @description 根据传入的事件和消息自动选择合适的 API 发送消息
     * @date 2025/6/13 10:04
     **/
    void sendMessage(PostEvent event, Message message);

    void sendPrivateMessage(Long userId, Message message);

    void sendGroupMessage(Long groupId, Message message);

}
