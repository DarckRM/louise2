package akarin.bot.louise2.domain.gakumas.cards;

import akarin.bot.louise2.domain.gakumas.ShowcaseContext;
import akarin.bot.louise2.domain.gakumas.effct.Effect;
import akarin.bot.louise2.domain.gakumas.idols.LogicalIdol;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.function.Consumer;

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

    private Integer upgradeCost = 0;

    private Integer activeCount = 1;

    private Integer upgradeActiveCount = 0;

    private Integer nicePoint = 3;

    private Integer upgradeNicePoint = 0;

    private Integer bonus = 1;

    private Integer upgradeBonus = 0;

    private String description = String.format("好印象专用, 发动时获得 %s 点好印象, 后续每次使用 Active 卡时增加 %s 点好印象",
            nicePoint(), bonus());

    private Integer nicePoint() {
        return getNicePoint() + getUpgradeNicePoint();
    }

    private Integer bonus() {
        return getBonus() + getUpgradeBonus();
    }

    private Consumer<Card> defaultUpgrade = c -> {
        this.setCost(3);
        this.setDescription(String.format("好印象专用, 发动时获得 %s 点好印象, 后续每次使用 Active 卡时增加 %s 点好印象, " +
                "本回合出牌数加 1 并且下一张发动卡牌额外生效 1 次", nicePoint(), bonus()));

        this.effect.setPlayCardEffect(ctx -> {
            // 当前回合出牌数加 1
            ctx.getCurrentTurn().increasePlayCount();
            // 当前回合下一张牌生效次数加 1
            ctx.getCurrentTurn().setActiveCount(2);
            ctx.getCurrentTurn().getEffect().setPlayCardEffect(ctx1 -> ctx1.getCurrentTurn().setActiveCount(1));
        });
    };

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

    @Override
    public void upgrade() {
        this.defaultUpgrade.accept(this);
    }

    @Override
    public void customize() {

    }

}
