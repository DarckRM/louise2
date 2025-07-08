package akarin.bot.louise2.domain.gakumas;

import akarin.bot.louise2.domain.gakumas.cards.Card;
import akarin.bot.louise2.domain.gakumas.effct.Effect;
import akarin.bot.louise2.domain.gakumas.exceptions.SkipTurnException;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * @author akarin
 * @version 1.0
 * @description 回合
 * @date 2025/7/3 16:28
 */
@Accessors(chain = true)
@Data
@Slf4j
public class Turn {

    // 回合类型
    public enum TurnType {
        VOCAL,
        DANCE,
        VISUAL;
    }

    private ShowcaseContext context;

    private TurnType type;

    private Effect effect = new Effect("回合中效果");

    // 当前回合抽牌数 默认 3 张
    private Integer drawCount = 3;

    // 当前回合出牌数 默认 1 张
    private Integer cardCount = 1;

    // 当前回合卡牌生效数 默认 1 次
    private Integer activeCount = 1;

    // 当前选择打出的卡牌
    private Card currentCard;

    // 当前回合出牌记录
    private List<Card> cardHistory = new ArrayList<>();

    public Turn(TurnType type, ShowcaseContext context) {
        this.context = context;
        this.type = type;
    }

    public void increasePlayCount() {
        this.cardCount++;
    }

    public void decreasePlayCount() {
        this.cardCount--;
    }

    public void play() {
        currentCard = null;
        int playedCardCount = 0;
        while (playedCardCount < getCardCount()) {
            // 输出手牌情况
            context.handPreview();
            try {
                // 玩家选择出牌
                peekCard();
                // 出牌逻辑
                playCard();
                // 出牌数增加
                playedCardCount++;
            } catch (SkipTurnException e) {
                log.info(e.getMessage());
                // 跳过回合恢复 2 点体力
                context.getKawaiiIdol().staminaRecover(2);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                // 出牌异常 如果选择了卡牌将其重新加入手牌
                log.error(e.getMessage());
                if (currentCard != null)
                    context.getHand().add(currentCard);
            }
        }
    }

    private void playCard() {
        // 体力处理
        context.staminaProcess(currentCard);
        for (int i = 0; i < activeCount; i++) {
            // 出牌阶段效果生效
            context.getEffects().forEach(e -> e.playCard(context));
            // 卡牌效果生效
            context.cardAffect(currentCard);
        }
        Optional.ofNullable(effect.getPlayCardEffect()).ifPresent(e -> e.affect(context));
        // 记录出牌记录
        getCardHistory().add(currentCard);
    }

    private void peekCard() {
        while (true) {
            try {
                Scanner scan = new Scanner(System.in);
                int cardIndex = scan.nextInt();
                if (cardIndex < 0)
                    throw new SkipTurnException("跳过出牌阶段");
                currentCard = context.getHand().remove(cardIndex);
                return;
            } catch (IndexOutOfBoundsException _) {
                log.error("出牌序号非法，请重试");
            }
        }
    }

}
