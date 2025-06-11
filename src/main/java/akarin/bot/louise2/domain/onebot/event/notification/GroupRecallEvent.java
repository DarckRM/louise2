package akarin.bot.louise2.domain.onebot.event.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author akarin
 * @version 1.0
 * @description 群消息撤回事件
 * @date 2025/6/11 11:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupRecallEvent extends NotificationEvent {

    @JsonProperty("operator_id")
    private Long operatorId;

    @JsonProperty("message_id")
    private Long messageId;

}
