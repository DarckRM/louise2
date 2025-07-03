
package akarin.bot.louise2.domain.gakumas.effct;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author akarin
 * @version 1.0
 * @description 效果基础类
 * @date 2025/7/3 17:25
 */

@Accessors(chain = true)
@Data
public class Effect {
    // 回合开始生效
    private EffectInterface turnStartEffect;
    // 抽卡阶段开始生效
    private EffectInterface drawEffect;
    // 回合结束阶段生效
    private EffectInterface turnEndEffect;
}
