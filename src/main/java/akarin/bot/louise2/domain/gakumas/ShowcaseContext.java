package akarin.bot.louise2.domain.gakumas;

import akarin.bot.louise2.domain.gakumas.cards.CardInterface;
import akarin.bot.louise2.domain.gakumas.idols.BaseIdol;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author akarin
 * @version 1.0
 * @description 展示环节上下文
 * @date 2025/7/3 15:56
 */
@Accessors(chain = true)
@Data
public class ShowcaseContext {

    // 目标得点
    private Long targetPoint;

    // 当前得点
    private Long currentPoint;

    // 该场展示所有回合
    private List<Turn> turns = new ArrayList<>();

    // 参与展示的小偶像
    private BaseIdol kawaiiIdol;

    // 当前的卡组
    private List<CardInterface> deck = new ArrayList<>();

    // 当前的手牌
    private List<CardInterface> hand = new ArrayList<>();

    // 丢弃的卡牌
    private List<CardInterface> discarded = new ArrayList<>();

    // 消耗的卡牌
    private List<CardInterface> destroyed = new ArrayList<>();

    // 当前持有的物品

    // 当前持有的饮料


    // 对应回合的单次打分数
    public void oneHit(Integer basePoint) {
        if (getTurns().isEmpty())
            return;

        Double point = switch (getTurns().getFirst().getType()) {
            case VOCAL -> basePoint * getKawaiiIdol().getVocalRate();
            case DANCE -> basePoint * getKawaiiIdol().getDanceRate();
            case VISUAL -> basePoint * getKawaiiIdol().getVisualRate();
        };

        setCurrentPoint(getCurrentPoint() + point.longValue());
    }

}
