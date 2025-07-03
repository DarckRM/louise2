package akarin.bot.louise2.domain.gakumas.effct;

import akarin.bot.louise2.domain.gakumas.ShowcaseContext;

/**
 * @author akarin
 * @version 1.0
 * @description 效果接口
 * @date 2025/7/3 16:09
 */
public interface EffectInterface {

    // 对某场演出执行效果
    void affect(ShowcaseContext context);

}
