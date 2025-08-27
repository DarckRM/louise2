package akarin.bot.louise2.domain.common;

import akarin.bot.louise2.domain.onebot.event.PostEvent;
import akarin.bot.louise2.domain.onebot.model.api.Message;
import akarin.bot.louise2.service.OnebotService;
import lombok.Data;

/**
 * @author akarin
 * @version 1.0
 * @description Louise 上下文
 * @date 2025/2/14 16:32
 */
@Data
public class LouiseContext {

    private PostEvent event;

    public LouiseContext(PostEvent event) {
        this.event = event;
    }
}
