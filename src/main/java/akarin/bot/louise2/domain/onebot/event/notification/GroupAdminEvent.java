package akarin.bot.louise2.domain.onebot.event.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author akarin
 * @version 1.0
 * @description 群管理员变动事件
 * @date 2025/6/11 11:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupAdminEvent extends NotificationEvent {
    @JsonProperty("sub_type")
    private String subType;
}
