package akarin.bot.louise2.domain.onebot.event.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author akarin
 * @version 1.0
 * @description 群内戳一戳事件
 * @date 2025/6/11 11:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupPokeEvent extends NotificationEvent {
    @JsonProperty("sub_type")
    String subType;

    @JsonProperty("target_id")
    Long targetId;
}
