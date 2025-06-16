package akarin.bot.louise2.domain.onebot.model.api;

import akarin.bot.louise2.domain.onebot.model.message.MessageSegmentInterface;

import java.util.List;

/**
 * @author akarin
 * @version 1.0
 * @description 消息实体接口
 * @date 2025/2/13 18:25
 */
public interface Message {

    List<MessageSegmentInterface> getMessages();

    Object toJSON();
}
