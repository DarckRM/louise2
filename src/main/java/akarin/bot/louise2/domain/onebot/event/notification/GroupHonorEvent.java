package akarin.bot.louise2.domain.onebot.event.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author akarin
 * @version 1.0
 * @description 群荣誉变更事件
 * @date 2025/6/11 11:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupHonorEvent extends NotificationEvent {
    // talkative(龙王), performer(群聊之火), emotion(快乐源泉)
    @JsonProperty("sub_type")
    String subType;
}
