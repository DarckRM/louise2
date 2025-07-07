package akarin.bot.louise2.domain.gakumas.cards;

import akarin.bot.louise2.domain.gakumas.ShowcaseContext;
import akarin.bot.louise2.domain.gakumas.effct.Effect;
import akarin.bot.louise2.domain.gakumas.idols.LogicalIdol;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author akarin
 * @version 1.0
 * @description 增强逻辑
 * @date 2025/7/7 16:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EnhanceLogicalCard extends BaseCard implements Card {

    private Effect effect = new Effect("增强逻辑");

    private CardType type = CardType.MENTAL;

    private String name = "增强逻辑";

    private Integer cost = 5;

    private String description = "好印象专用, 发动时获得 3 点好印象, 后续每次使用 Active 卡时增加 1 点好印象";

    private Integer activeCount = 1;

    public EnhanceLogicalCard() {
        effect.setActiveCardEffect(ctx -> {
            LogicalIdol kawaiiIdol = (LogicalIdol) ctx.getKawaiiIdol();
            if (ctx.getCurrentTurn().getCardHistory().getLast().getType() == CardType.ACTIVE)
                kawaiiIdol.getNiceImpression().increase(3);
        });

        effect.setPlayCardEffect(ctx -> {
            LogicalIdol kawaiiIdol = (LogicalIdol) ctx.getKawaiiIdol();
            kawaiiIdol.getNiceImpression().increase(1);
        });
    }

    @Override
    public void affect(ShowcaseContext context) {
        effect.getActiveCardEffect().affect(context);
        context.getEffects().add(effect);
    }

}
