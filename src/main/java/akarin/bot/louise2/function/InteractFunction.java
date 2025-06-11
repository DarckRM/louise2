package akarin.bot.louise2.function;

import akarin.bot.louise2.domain.onebot.event.api.PostEventInterface;

/**
 * @author akarin
 * @version 1.0
 * @description 交互式函数接口
 * @date 2025/6/6 17:45
 */
public interface InteractFunction<T> {

    T execute(PostEventInterface event);

}
