package akarin.bot.louise2.domain.gakumas.cards;

import akarin.bot.louise2.domain.gakumas.idols.BaseIdol;
import lombok.Data;
import lombok.Generated;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Optional;

/**
 * @author akarin
 * @version 1.0
 * @description 卡牌
 * @date 2025/7/3 15:51
 */
@Data
public class BaseCard {

    public enum CardType {
        ACTIVE(0, "Active"),
        MENTAL(1, "Mental");

        @Getter
        private final Integer key;
        @Getter
        private final String value;

        CardType(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private String id;

    private String name;

    private BaseIdol.IdolType idolType;

    // active: 打分; mental: buff
    private CardType type;

    private Integer cost;

    private Integer point;

    private Integer activeCount;

    private String description;

    public String description() {
        StringBuilder builder = new StringBuilder();
//        builder.append(getId()).append(" ");
        builder.append(getName()).append(" ");
        builder.append("(").append(getType().getValue()).append(") ");
        builder.append("体力消耗: (").append(getCost()).append(") ");
        Optional.ofNullable(getPoint()).ifPresent(e -> builder.append("Pt: (").append(e).append(") "));
        builder.append(getDescription());
        Optional.ofNullable(getActiveCount()).ifPresent(e -> builder.append("(演出中限定 ")
                .append(getActiveCount()).append(" 次)"));
        return builder.toString();
    }
}
