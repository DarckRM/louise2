package akarin.bot.louise2.domain.gakumas.cards;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author akarin
 * @version 1.0
 * @description 卡牌
 * @date 2025/7/3 15:51
 */
@Accessors(chain = true)
@Data
public class BaseCard {

    private String id;

    private String name;

    // active: 打分; mental: buff
    private Integer type;

    private Integer cost;

    private Integer point;

    private Integer bonus;
}
