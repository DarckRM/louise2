package akarin.bot.louise2.domain.onebot.event;

import akarin.bot.louise2.domain.common.LouiseContext;
import akarin.bot.louise2.domain.onebot.event.api.PostEventInterface;
import akarin.bot.louise2.domain.onebot.model.api.Message;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


/**
 * @author akarin
 * @version 1.0
 * @description 上报事件
 * @date 2025/2/14 14:17
 */
@Data
public class PostEvent implements PostEventInterface {

    private LouiseContext context;

    private Long time;

    @JsonProperty("self_id")
    private Long selfId;

    @JsonProperty("post_type")
    private String postType;

    public void reply(Message message) {
        context.reply(message);
    }

}
