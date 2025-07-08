package akarin.bot.louise2.domain.gakumas.cards;

import akarin.bot.louise2.domain.gakumas.ShowcaseContext;

/**
 * @author akarin
 * @version 1.0
 * @description 卡片接口
 * @date 2025/7/3 16:17
 */
public interface Card {

    String getName();

    void setName(String name);

    BaseCard.CardType getType();

    void setType(BaseCard.CardType type);

    Integer getActiveCount();

    void setActiveCount(Integer activeCount);

    /**
     * @return java.lang.Integer
     * @author akarin
     * @description 获取卡牌发动次数
     * @date 2025/7/8 16:51
     **/
    Integer active();

    /**
     * @return java.lang.Integer
     * @author akarin
     * @description 获取卡牌使用次数
     * @date 2025/7/8 16:47
     **/
    Integer count();

    /**
     * @author akarin
     * @description 增加卡牌使用次数
     * @date 2025/7/8 16:49
     **/
    void increaseCount();

    /**
     * @author akarin
     * @description 减少卡牌使用次数
     * @date 2025/7/8 16:48
     **/
    void decreaseCount();

    /**
     * @return java.lang.Integer
     * @author akarin
     * @description 获取卡牌 Pt 点数
     * @date 2025/7/8 16:45
     **/
    Integer point();

    /**
     * @return java.lang.Integer
     * @author akarin
     * @description 获取卡牌体力消耗
     * @date 2025/7/8 16:46
     **/
    Integer cost();

    /**
     * @return java.lang.String
     * @author akarin
     * @description 获取卡牌的详细描述
     * @date 2025/7/8 11:46
     **/
    String description();

    /**
     * @param context 某场演出的上下文
     * @author akarin
     * @description 卡牌生效
     * @date 2025/7/8 11:47
     **/
    void affect(ShowcaseContext context);

    /**
     * @author akarin
     * @description 卡牌强化
     * @date 2025/7/8 11:55
     **/
    void upgrade();

    /**
     * @author akarin
     * @description 卡牌定制化
     * @date 2025/7/8 11:55
     **/
    void customize();

}
