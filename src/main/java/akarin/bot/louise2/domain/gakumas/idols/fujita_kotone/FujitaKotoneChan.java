package akarin.bot.louise2.domain.gakumas.idols.fujita_kotone;

import akarin.bot.louise2.domain.gakumas.effct.EffectInterface;
import akarin.bot.louise2.domain.gakumas.idols.BaseIdol;
import akarin.bot.louise2.domain.gakumas.idols.IdolInterface;
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
public class FujitaKotoneChan extends LogicalIdol implements IdolInterface {

    private IdolType type = IdolType.LOGICAL;

    // 每次发动技能好印象 +2, 并且在最后 3 回合基于好印象打分
    private EffectInterface inherentSkill = context -> {
        FujitaKotoneChan kotone = (FujitaKotoneChan) context.getKawaiiIdol();

        if (context.getTurns().size() < 3)
            context.oneHit(kotone.getNiceExperience());

        kotone.setNiceExperience(kotone.getNiceExperience() + 2);
    };

}
