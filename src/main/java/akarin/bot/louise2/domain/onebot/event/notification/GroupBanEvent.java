package akarin.bot.louise2.domain.onebot.event.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author akarin
 * @version 1.0
 * @description 群禁言事件
 * @date 2025/6/11 11:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupBanEvent extends NotificationEvent {
    // ban, lift_ban
    @JsonProperty("sub_type")
    private String subType;

    private Integer duration;
}
