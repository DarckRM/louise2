package akarin.bot.louise2.domain.gakumas;

import akarin.bot.louise2.domain.gakumas.cards.Card;
import akarin.bot.louise2.domain.gakumas.cards.EnhanceLogicalCard;
import akarin.bot.louise2.domain.gakumas.cards.NiceHitCard;
import akarin.bot.louise2.domain.gakumas.effct.Effect;
import akarin.bot.louise2.domain.gakumas.exceptions.RunOutOfStaminaException;
import akarin.bot.louise2.domain.gakumas.idols.Idol;
import akarin.bot.louise2.domain.gakumas.idols.fujita_kotone.FujitaKotoneChan;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.random.RandomGenerator;

/**
 * @author akarin
 * @version 1.0
 * @description 展示环节上下文
 * @date 2025/7/3 15:56
 */
@Accessors(chain = true)
@Data
@Slf4j
public class ShowcaseContext {

    private final RandomGenerator random = new Random();

    // 目标得点
    private Long targetPoint;

    // 当前得点
    private Long currentPoint;

    // 该场演出所有回合
    private List<Turn> turns = new ArrayList<>();

    // 该场演出历史回合
    private List<Turn> hisTurns = new ArrayList<>();

    // 当前回合
    private Turn currentTurn;

    // 参与展示的小偶像
    private Idol kawaiiIdol;

    // 当前所有效果
    private List<Effect> effects = new ArrayList<>();

    // 当前的牌山
    private List<Card> deck = new ArrayList<>();

    // 当前的手牌
    private List<Card> hand = new ArrayList<>();

    // 丢弃的卡牌
    private List<Card> discarded = new ArrayList<>();

    // 消耗的卡牌
    private List<Card> destroyed = new ArrayList<>();

    // 当前持有的物品

    // 当前持有的饮料

    // 演出开始
    public void showcase() {
        // 初始化点数
        setCurrentPoint(0L);
        setEffects(new ArrayList<>());

        // 初始化回合数
        Random random = new Random();
        for (int i = 0; i < 11; i++)
            getTurns().add(new Turn(Turn.TurnType.values()[random.nextInt(Turn.TurnType.values().length)], this));

        // 偶像属性初始化
        getKawaiiIdol().init(this);
        // 偶像天赋效果生效
        getEffects().add(getKawaiiIdol().getTalent().getEffect());
        getEffects().add(getKawaiiIdol().getInherentSkill());
        // 圣遗物效果生效

        // 演出阶段
        int turn = 1;
        while (!turns.isEmpty()) {
            log.info("====== 第 {} 回合 ======", turn);
            currentTurn = turns.removeFirst();
            // 回合开始
            getEffects().forEach(e -> e.turnStart(this));

            // 抽卡阶段
            drawCards();
            getEffects().forEach(e -> e.drawCard(this));

            // 出牌阶段
            contextPreview();
            currentTurn.play();

            // 弃牌阶段
            discardCard();

            // 回合结束
            getEffects().forEach(e -> e.turnEnd(this));

            hisTurns.add(currentTurn);
            log.info("====== 回合 {} 结束 ======\n", turn);
            turn++;
        }

    }

    public void contextPreview() {
        log.info("当前分数 {}Pt 当前回合 {} 牌山数: {} 弃牌数: {} 除外数: {} ", getCurrentPoint(), getCurrentTurn().getType(),
                getDeck().size(), getDiscarded().size(), getDestroyed().size());
        log.info("偶像信息: {}", getKawaiiIdol().description());
        // 当前生效效果
        log.info("持续效果: {}", getEffects().stream().map(e -> "「" + e.getName() + "」").toList());
    }

    public void discardCard() {
        // 剩余手牌进入弃牌区
        discarded.addAll(hand);
        hand.clear();
    }

    public void staminaProcess(Card card) {
        // 体力扣除计算
        if (card.cost() > getKawaiiIdol().getStamina())
            throw new RunOutOfStaminaException();
        getKawaiiIdol().staminaCost(card.cost());
    }


    public void handPreview() {
        log.info("手牌数: {}", hand.size());
        hand.forEach(c -> log.info(c.description()));
    }

    public void cardAffect(Card card) {
        // 卡牌效果发动
        log.info("卡牌发动: 「{}」 ", card.getName());
        card.decreaseCount();
        for (int i = 0; i < card.active(); i++)
            card.affect(this);
        // 使用的手牌如果使用次数为 0 进入除外区
        if (card.count() == 0)
            destroyed.add(card);
        else
            // 否则进入弃牌区
            discarded.add(card);
    }

    public void drawCardsAdditional(Integer count) {
        // 如果牌山已经抽空, 则将弃牌区所有卡牌加入到牌山
        if (deck.isEmpty()) {
            deck.addAll(discarded);
            discarded.clear();
        }

        while (count > 0 && !deck.isEmpty()) {
            int peek = random.nextInt(deck.size());
            Card drawnCard = deck.remove(peek);
            hand.add(drawnCard);
            count--;
        }
    }

    public void drawCards() {
        int drawCount = currentTurn.getDrawCount();
        // 如果牌山已经抽空, 则将弃牌区所有卡牌加入到牌山
        if (deck.isEmpty()) {
            deck.addAll(discarded);
            discarded.clear();
        }

        while (drawCount > 0 && !deck.isEmpty()) {
            int peek = random.nextInt(deck.size());
            Card drawnCard = deck.remove(peek);
            hand.add(drawnCard);
            drawCount--;
        }
    }

    // 对应回合的单次打分数
    public void oneHit(Integer basePoint) {
        Double point = switch (getCurrentTurn().getType()) {
            case VOCAL -> basePoint * getKawaiiIdol().getVocalRate();
            case DANCE -> basePoint * getKawaiiIdol().getDanceRate();
            case VISUAL -> basePoint * getKawaiiIdol().getVisualRate();
        };
        log.info("| Pt UP: {}", point.longValue());
        setCurrentPoint(getCurrentPoint() + point.longValue());
    }

    public static void main(String[] args) {
        FujitaKotoneChan kotone = new FujitaKotoneChan();
        kotone.setVocal(100.0);
        kotone.setDance(100.0);
        kotone.setVisual(100.0);
        kotone.setVocalRate(1.4);
        kotone.setDanceRate(1.2);
        kotone.setVisualRate(2.4);
        kotone.getNiceImpression().setValue(50);
        ShowcaseContext context = new ShowcaseContext();
        context.setKawaiiIdol(kotone);
        for (int i = 0; i < 7; i++) {
            NiceHitCard niceHitCard = new NiceHitCard();
            niceHitCard.setId(String.valueOf(i));
            context.getDeck().add(niceHitCard);
        }
        EnhanceLogicalCard enhance = new EnhanceLogicalCard();
        enhance.upgrade();
        context.getDeck().add(enhance);
        context.showcase();
        context.contextPreview();
    }

}
