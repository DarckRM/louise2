package akarin.bot.louise2.domain.common;

import akarin.bot.louise2.domain.onebot.event.PostEvent;
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

}
