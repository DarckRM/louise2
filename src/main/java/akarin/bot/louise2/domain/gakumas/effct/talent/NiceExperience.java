package akarin.bot.louise2.domain.gakumas.effct.talent;

import akarin.bot.louise2.domain.gakumas.effct.Effect;
import akarin.bot.louise2.domain.gakumas.idols.BaseIdol;
import akarin.bot.louise2.domain.gakumas.idols.LogicalIdol;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author akarin
 * @version 1.0
 * @description 好印象: 回合结束时基于倍率和好印象值打分然后好印象减 1
 * @date 2025/7/3 16:54
 */
@Accessors(chain = true)
@Data
public class NiceExperience implements Talent {

    private Integer value;

    private Effect effect = new Effect("好印象");

    public void increase(Integer amount) {
        this.value += amount;
    }

    public void decrease(Integer amount) {
        this.value -= amount;
    }

    public NiceExperience() {
        // 回合结束时基于好印象和倍率打分然后减 1
        effect.setTurnEndEffect(ctx -> {
            ctx.oneHit(this.getValue());
            this.decrease(1);
        });
    }

}
