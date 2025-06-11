package akarin.bot.louise2.domain.onebot.event.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author akarin
 * @version 1.0
 * @description 群成员增加事件
 * @date 2025/6/11 11:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupIncreaseEvent extends NotificationEvent {

    @JsonProperty("operator_id")
    private String operatorId;

    // approve, invite
    @JsonProperty("sub_type")
    private String subType;
}
