package akarin.bot.louise2.domain.gakumas.cards;

import akarin.bot.louise2.domain.gakumas.ShowcaseContext;
import akarin.bot.louise2.domain.gakumas.custom.Customization;
import akarin.bot.louise2.domain.gakumas.effct.Effect;
import akarin.bot.louise2.domain.gakumas.idols.LogicalIdol;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;

/**
 * @author akarin
 * @version 1.0
 * @description 测试用卡片
 * @date 2025/7/3 16:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NiceHitCard extends BaseCard implements Card {

    private Effect effect = new Effect("基础表演");

    private CardType type = CardType.ACTIVE;

    private String name = "基础表演";

    private Integer count = 1;

    private Integer point = 10;

    private Integer cost = 3;

    private Integer activeCount = 1;

    private Integer bonus = 2;

    private String description = "好印象专用, 发动时获得 " + getPoint() + "Pt 分数, 并且增加 " + getBonus() + " 点好印象";

    private Customize<NiceHitCard> customize = new Customize<>();

    private Integer impression = 2;

    public NiceHitCard() {
        effect.setActiveCardEffect(ctx -> {
            LogicalIdol kawaiiIdol = (LogicalIdol) ctx.getKawaiiIdol();
            ctx.oneHit(getPoint());
            kawaiiIdol.getNiceImpression().increase(impression);
        });

        HashMap<Customize.CustomType, List<Customization<NiceHitCard>>> tempCustom = new HashMap<>();

        // 附魔(数值强化) 1: 好印象固定增加 4; 2: 好印象固定增加 6
        tempCustom.put(Customize.CustomType.NUMERIC_ENHANCE, Arrays.asList(c -> c.setImpression(4),
                c -> c.setImpression(6)));

        tempCustom.put(Customize.CustomType.ADDITION_EFFECT, Collections.singletonList(c -> {
            c.getEffect().setActiveCardEffect(ctx -> {
                LogicalIdol kawaiiIdol = (LogicalIdol) ctx.getKawaiiIdol();
                ctx.oneHit(getPoint());
                kawaiiIdol.getNiceImpression().increase(impression);
                // 附魔(额外效果) 1: 额外抽一张卡以及再出一张牌
                ctx.drawCardsAdditional(1);
                ctx.getCurrentTurn().increasePlayCount();
            });
        }));
        customize.getCustomMap().putAll(tempCustom);
    }

    @Override
    public void affect(ShowcaseContext context) {
        effect.getActiveCardEffect().affect(context);
    }

    @Override
    public void upgrade() {

    }

    @Override
    public void customize() {

    }

}
