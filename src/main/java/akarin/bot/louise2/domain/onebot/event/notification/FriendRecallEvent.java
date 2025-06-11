package akarin.bot.louise2.domain.onebot.event.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author akarin
 * @version 1.0
 * @description 好友消息撤回事件
 * @date 2025/6/11 11:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FriendRecallEvent extends NotificationEvent {
    @JsonProperty("message_id")
    private long messageId;
}
