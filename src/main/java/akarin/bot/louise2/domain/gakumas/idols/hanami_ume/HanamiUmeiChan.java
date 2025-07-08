package akarin.bot.louise2.domain.gakumas.idols.hanami_ume;

import akarin.bot.louise2.domain.gakumas.ShowcaseContext;
import akarin.bot.louise2.domain.gakumas.effct.Effect;
import akarin.bot.louise2.domain.gakumas.effct.talent.Talent;
import akarin.bot.louise2.domain.gakumas.effct.talent.Yaruki;
import akarin.bot.louise2.domain.gakumas.idols.Idol;
import akarin.bot.louise2.domain.gakumas.idols.LogicalIdol;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author akarin
 * @version 1.0
 * @description 花海 Ume
 * @date 2025/7/7 17:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HanamiUmeiChan extends LogicalIdol implements Idol {

    private String name = "花海佑芽 (Hanami Umei)";

    private IdolType type = IdolType.LOGICAL;

    private Effect inherentSkill = new Effect("元气说是");

    private Yaruki yaruki = new Yaruki();

    private Integer stamina = 12;

    private Integer maxStamina = 12;

    public HanamiUmeiChan() {
        // 演出开始时干劲每回合加 3 并且获得 5 元气值, 持续 3 回合
        getInherentSkill().setTurnStartEffect(ctx -> {
            if (ctx.getHisTurns().size() < 3) {
                getYaruki().increase(3);
                setGenki(getGenki() + 5 + getYaruki().getValue());
            }
        });
    }

    @Override
    public void init(ShowcaseContext context) {

    }

    @Override
    public Talent getTalent() {
        return null;
    }

    @Override
    public String description() {
        return "";
    }
}
