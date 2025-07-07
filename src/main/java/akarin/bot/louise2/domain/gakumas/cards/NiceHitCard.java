package akarin.bot.louise2.domain.gakumas.cards;

import akarin.bot.louise2.domain.gakumas.ShowcaseContext;
import akarin.bot.louise2.domain.gakumas.effct.Effect;
import akarin.bot.louise2.domain.gakumas.effct.EffectInterface;
import akarin.bot.louise2.domain.gakumas.idols.Idol;
import akarin.bot.louise2.domain.gakumas.idols.LogicalIdol;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author akarin
 * @version 1.0
 * @description 测试用卡片
 * @date 2025/7/3 16:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NiceHitCard extends BaseCard implements Card {

    private Effect effect = new Effect();

    private CardType type = CardType.ACTIVE;

    private String name = "基础表演";

    private Integer point = 10;

    private Integer cost = 3;

    private String description = "好印象专用, 发动时获得 " + getPoint() + "Pt 分数, 并且增加 2 点好印象";

    private Integer activeCount = 1;

    public NiceHitCard() {
        effect.setActiveEffect(ctx -> {
            LogicalIdol kawaiiIdol = (LogicalIdol) ctx.getKawaiiIdol();
            ctx.oneHit(getPoint());
            kawaiiIdol.getNiceExperience().increase(2);
        });
    }

    @Override
    public void affect(ShowcaseContext context) {
        effect.getActiveEffect().affect(context);
    }

}
