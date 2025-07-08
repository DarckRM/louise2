package akarin.bot.louise2.domain.gakumas.cards;

import akarin.bot.louise2.domain.gakumas.ShowcaseContext;
import akarin.bot.louise2.domain.gakumas.effct.Effect;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author akarin
 * @version 1.0
 * @description 强化接下来的两张卡
 * @date 2025/7/8 12:03
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpgradeNext2Card extends BaseCard implements Card {

    private Effect effect = new Effect("强化增幅");

    private CardType type = CardType.MENTAL;

    private String name = "强化增幅";

    private Integer cost = 6;

    private String description = "发动时获得 " + getBonus() + " 点元气, 接下来 " + getTurns() + " 回合强化抽到的手牌";

    private Integer activeCount = 1;

    private Integer bonus = 5;

    private Integer turns = 2;

    @Override
    public void affect(ShowcaseContext context) {

    }

    @Override
    public void upgrade() {

    }

    @Override
    public void customize() {

    }
}
