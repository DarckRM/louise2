package akarin.bot.louise2.domain.gakumas.cards;

import akarin.bot.louise2.domain.gakumas.idols.BaseIdol;
import lombok.Data;
import lombok.Getter;

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

    // 卡牌 ID
    private String id;

    // 卡牌名称
    private String name;

    // 偶像类型
    private BaseIdol.IdolType idolType;

    // active: 打分; mental: buff
    private CardType type;

    // 演出中卡牌可使用次数
    private Integer count;

    private Integer countBonus = 0;

    // 体力消耗
    private Integer cost;

    private Integer costBonus = 0;

    // 得分 Pt 数
    private Integer point;

    private Integer pointBonus = 0;

    // 卡牌生效次数
    private Integer activeCount;

    private Integer activeCountBonus = 0;

    // 效果描述
    private String description;

    public Integer count() {
        if (getCount() == null)
            return null;
        return getCount() + getCountBonus();
    }

    public void increaseCount() {
        if (this.count == null)
            return;
        this.count++;
    }

    public void decreaseCount() {
        if (this.count == null)
            return;
        this.count--;
    }

    public Integer active() {
        return getActiveCount() + getActiveCountBonus();
    }

    public Integer point() {
        if (getPoint() == null)
            return null;
        return getPoint() + getPointBonus();
    }

    public Integer cost() {
        return getCost() + getCostBonus();
    }

    public String description() {
        StringBuilder builder = new StringBuilder();
        builder.append(getName()).append(" ");
        builder.append("(").append(getType().getValue()).append(") ");
        builder.append("体力消耗: (").append(cost()).append(") ");
        Optional.ofNullable(point()).ifPresent(e -> builder.append("Pt: (").append(e).append(") "));
        builder.append(getDescription());
        Optional.ofNullable(active()).ifPresent(e -> builder.append("(生效 ")
                .append(active()).append(" 次)"));
        Optional.ofNullable(count()).ifPresent(e -> builder.append("(演出中限定 ")
                .append(count()).append(" 次)"));
        return builder.toString();
    }
}
