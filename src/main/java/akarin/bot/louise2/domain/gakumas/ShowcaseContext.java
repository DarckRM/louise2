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
            getTurns().add(new Turn(Turn.TurnType.values()[random.nextInt(Turn.TurnType.values().length)]));

        // 偶像属性初始化
        getKawaiiIdol().init(this);
        // 偶像天赋效果生效
        getEffects().add(getKawaiiIdol().getTalent().getEffect());
        getEffects().add(getKawaiiIdol().getInherentSkill());
        // 圣遗物效果生效

        // 演出阶段
        while (!turns.isEmpty()) {
            currentTurn = turns.removeFirst();
            // 回合开始
            getEffects().forEach(e -> e.turnStart(this));

            // 抽卡阶段
            drawCards();
            getEffects().forEach(e -> e.drawCard(this));

            // 出牌阶段
            contextPreview();
            playCard();

            // 弃牌阶段
            discardCard();

            // 回合结束
            getEffects().forEach(e -> e.turnEnd(this));

            hisTurns.add(currentTurn);
        }

    }

    public void contextPreview() {
        log.info("当前分数 {}Pt 当前回合 {} 牌山数: {} 弃牌数: {} 除外数: {} ", getCurrentPoint(), getCurrentTurn().getType(),
                getDeck().size(), getDiscarded().size(), getDestroyed().size());
        log.info("演出偶像: {}", getKawaiiIdol().description());
    }

    public void discardCard() {
        // 剩余手牌进入弃牌区
        discarded.addAll(hand);
        hand.clear();
    }

    public void playCard() {
        int cardCount = currentTurn.getCardCount();
        Card card = null;
        while (cardCount > 0) {
            try {
                handPreview();
                card = peekCard();

                staminaProcess(card);

                // 出牌阶段效果生效
                getEffects().forEach(e -> e.playCard(this));

                cardAffect(card);
                cardCount--;
                getCurrentTurn().getCardHistory().add(card);
            } catch (IllegalArgumentException e) {
                log.info(e.getMessage());
                // 跳过回合恢复 2 点体力
                getKawaiiIdol().staminaRecover(2);
                return;
            } catch (Exception e) {
                log.error(e.getMessage());
                if (card != null)
                    hand.add(card);
            }
        }

    }

    private void staminaProcess(Card card) {
        // 体力扣除计算
        if (card.getCost() > getKawaiiIdol().getStamina())
            throw new RunOutOfStaminaException();
        getKawaiiIdol().staminaCost(card.getCost());
    }

    private void handPreview() {
        log.info("手牌数: {}", hand.size());
        hand.forEach(c -> log.info(c.description()));
    }

    private Card peekCard() {
        while (true) {
            try {
                Scanner scan = new Scanner(System.in);
                int cardIndex = scan.nextInt();
                if (cardIndex < 0)
                    throw new IllegalArgumentException("跳过出牌阶段");
                return hand.remove(cardIndex);
            } catch (IndexOutOfBoundsException _) {
                log.error("出牌序号非法，请重试");
            }
        }
    }

    private void cardAffect(Card card) {
        // 卡牌效果发动
        log.info("卡牌发动: {} ", card.description());
        card.affect(this);
        card.setActiveCount(card.getActiveCount() - 1);
        // 使用的手牌如果使用次数为 0 进入除外区
        if (card.getActiveCount() == 0)
            destroyed.add(card);
        else
            // 否则进入弃牌区
            discarded.add(card);
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
        log.info("获得分数: {}Pt", point.longValue());
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
        kotone.getNiceExperience().setValue(50);
        ShowcaseContext context = new ShowcaseContext();
        context.setKawaiiIdol(kotone);
        for (int i = 0; i < 7; i++) {
            NiceHitCard niceHitCard = new NiceHitCard();
            niceHitCard.setId(String.valueOf(i));
            context.getDeck().add(niceHitCard);
        }
        context.getDeck().add(new EnhanceLogicalCard());
        context.showcase();
        context.contextPreview();
    }

}
