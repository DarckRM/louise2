package akarin.bot.louise2.domain.gakumas;

import akarin.bot.louise2.domain.gakumas.cards.Card;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author akarin
 * @version 1.0
 * @description 回合
 * @date 2025/7/3 16:28
 */
@Accessors(chain = true)
@Data
public class Turn {

    // 回合类型
    public enum TurnType {
        VOCAL,
        DANCE,
        VISUAL;
    }

    private TurnType type;

    // 当前回合抽牌数 默认 3 张
    private Integer drawCount = 3;

    // 当前回合出牌数 默认 1 张
    private Integer cardCount = 1;

    // 当前回合出牌记录
    private List<Card> cardHistory = new ArrayList<>();

    public Turn(TurnType type) {
        this.type = type;
    }

}
