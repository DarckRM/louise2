package akarin.bot.louise2.domain.onebot.event.meta;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author akarin
 * @version 1.0
 * @description 心跳事件
 * @date 2025/2/14 17:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HeartbeatEvent extends LifecycleEvent {

    private JSONObject status;

    private Long interval;
}
