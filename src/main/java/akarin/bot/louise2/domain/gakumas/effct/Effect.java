
package akarin.bot.louise2.domain.gakumas.effct;

import akarin.bot.louise2.domain.gakumas.ShowcaseContext;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * @author akarin
 * @version 1.0
 * @description 效果基础类
 * @date 2025/7/3 17:25
 */

@Accessors(chain = true)
@Data
@Slf4j
public class Effect {

    private String name;

    // 回合开始生效
    private EffectInterface turnStartEffect;
    // 抽卡阶段开始生效
    private EffectInterface drawCardEffect;
    // 出牌阶段开始生效
    private EffectInterface playCardEffect;
    // 回合结束阶段生效
    private EffectInterface turnEndEffect;
    // 主动发动时生效
    private EffectInterface activeCardEffect;

    public void turnStart(ShowcaseContext ctx) {
        if (getTurnStartEffect() == null)
            return;
        log.info("-- 回合开始 「{}」 发动", getName());
        getTurnStartEffect().affect(ctx);
    }

    public void drawCard(ShowcaseContext ctx) {
        if (getDrawCardEffect() == null)
            return;
        log.info("-- 抽卡阶段 「{}」 发动", getName());
        getDrawCardEffect().affect(ctx);
    }

    public void playCard(ShowcaseContext ctx) {
        if (getPlayCardEffect() == null)
            return;
        log.info("-- 出牌阶段 「{}」 发动", getName());
        getPlayCardEffect().affect(ctx);
    }

    public void turnEnd(ShowcaseContext ctx) {
        if (getTurnEndEffect() == null)
            return;
        log.info("-- 回合结束 「{}」 发动", getName());
        getTurnEndEffect().affect(ctx);
    }

    public void active(ShowcaseContext ctx) {
        if (getActiveCardEffect() == null)
            return;
        log.info("-- 「{}」 主动发动", getName());
        getActiveCardEffect().affect(ctx);
    }

    public Effect(String name) {
        this.name = name;
    }
}
