package akarin.bot.louise2.domain.onebot.event.request;

import akarin.bot.louise2.domain.onebot.event.PostEvent;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author akarin
 * @version 1.0
 * @description 请求事件
 * @date 2025/2/13 18:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RequestEvent extends PostEvent {

    @JsonProperty("request_type")
    private String requestType;

    @JsonProperty("user_id")
    private Long userId;

    private String comment;

    private String flag;

    @JsonProperty("sub_type")
    private String subType;

}
