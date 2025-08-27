package akarin.bot.louise2.domain.gakumas.custom;

import akarin.bot.louise2.domain.gakumas.cards.Card;

/**
 * @author akarin
 * @version 1.0
 * @description 附魔接口
 * @date 2025/7/21 17:37
 */
public interface Customization<T extends Card> {

    void custom(T card);

}
