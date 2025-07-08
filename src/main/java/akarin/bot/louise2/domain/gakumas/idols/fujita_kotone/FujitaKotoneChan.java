package akarin.bot.louise2.domain.gakumas.idols.fujita_kotone;

import akarin.bot.louise2.domain.gakumas.ShowcaseContext;
import akarin.bot.louise2.domain.gakumas.effct.Effect;
import akarin.bot.louise2.domain.gakumas.effct.talent.NiceImpression;
import akarin.bot.louise2.domain.gakumas.effct.talent.Talent;
import akarin.bot.louise2.domain.gakumas.idols.Idol;
import akarin.bot.louise2.domain.gakumas.idols.LogicalIdol;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author akarin
 * @version 1.0
 * @description 藤田琴音
 * @date 2025/7/3 16:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FujitaKotoneChan extends LogicalIdol implements Idol {

    private String name = "藤田琴音(Fujita Kotone)";

    private IdolType type = IdolType.LOGICAL;

    private Effect inherentSkill = new Effect("固有技能");

    private NiceImpression niceImpression = new NiceImpression();

    private Integer stamina = 12;

    private Integer maxStamina = 12;

    public FujitaKotoneChan() {
        // 每次发动技能好印象 +2, 并且在最后 3 回合基于好印象打分
        this.getInherentSkill().setTurnStartEffect(ctx -> {
            if (ctx.getTurns().size() < 3)
                ctx.oneHit(this.getNiceImpression().getValue());
            this.getNiceImpression().increase(2);
        });
    }

    public Talent getTalent() {
        return niceImpression;
    }

    @Override
    public void init(ShowcaseContext ctx) {
    }

    @Override
    public String description() {
        return getName() + " " +
                "体力: " + getStamina() + "/" + getMaxStamina() + " " +
                "好印象: " + getNiceImpression().getValue();
    }
}
