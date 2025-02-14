package akarin.bot.louise2.domain.onebot.event.meta;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author akarin
 * @version 1.0
 * @description 生命周期事件
 * @date 2025/2/14 16:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LifecycleEvent extends MetaEvent {

    @JsonProperty("sub_type")
    private String subType;
}
