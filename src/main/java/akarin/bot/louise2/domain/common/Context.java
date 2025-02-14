package akarin.bot.louise2.domain.common;

import akarin.bot.louise2.domain.onebot.event.PostEvent;
import lombok.Data;

/**
 * @author akarin
 * @version 1.0
 * @description 上下文
 * @date 2025/2/14 16:32
 */
@Data
public class Context {
    private PostEvent event;
}
