package akarin.bot.louise2.domain.onebot.event.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author akarin
 * @version 1.0
 * @description 群成员减少事件
 * @date 2025/6/11 11:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupDecreaseEvent extends NotificationEvent {

    @JsonProperty("operator_id")
    private String operatorId;

    // leave, kick, kick_me
    @JsonProperty("sub_type")
    private String subType;
}
