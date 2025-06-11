package akarin.bot.louise2.domain.onebot.event.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author akarin
 * @version 1.0
 * @description 群红包运气王事件
 * @date 2025/6/11 11:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LuckKingEvent extends NotificationEvent {
    // lucky_king
    @JsonProperty("sub_type")
    String subType;

    @JsonProperty("target_id")
    Long targetId;
}
