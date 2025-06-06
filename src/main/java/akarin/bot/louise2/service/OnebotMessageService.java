package akarin.bot.louise2.service;

import akarin.bot.louise2.domain.onebot.model.api.Message;

/**
 * @author akarin
 * @version 1.0
 * @description Onebot 协议消息 Service
 * @date 2025/6/6 14:18
 */
public interface OnebotMessageService {

    void sendPrivateMessage(Long userId, Message message);

    void sendGroupMessage(Long groupId, Message message);

}
