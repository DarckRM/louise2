package akarin.bot.louise2.domain.common;

import akarin.bot.louise2.domain.onebot.event.PostEvent;
import akarin.bot.louise2.domain.onebot.model.api.Message;
import akarin.bot.louise2.service.OnebotService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author akarin
 * @version 1.0
 * @description Louise 上下文
 * @date 2025/2/14 16:32
 */
@Data
public class LouiseContext {

    private PostEvent event;

    private OnebotService onebotService;

    public LouiseContext(PostEvent event, OnebotService onebotService) {
        this.event = event;
        this.onebotService = onebotService;
    }

// 回复消息
    public void reply(Message message) {
        onebotService.reply(event, message);
    }
}
