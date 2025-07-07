package akarin.bot.louise2.domain.gakumas.cards;

import akarin.bot.louise2.domain.gakumas.ShowcaseContext;

/**
 * @author akarin
 * @version 1.0
 * @description 卡片接口
 * @date 2025/7/3 16:17
 */
public interface Card {

    BaseCard.CardType getType();

    String description();

    void affect(ShowcaseContext context);

    Integer getActiveCount();

    void setActiveCount(Integer activeCount);

    Integer getCost();

}
