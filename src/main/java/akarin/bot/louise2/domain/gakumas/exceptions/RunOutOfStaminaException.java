package akarin.bot.louise2.domain.gakumas.exceptions;

/**
 * @author akarin
 * @version 1.0
 * @description 体力耗尽
 * @date 2025/7/7 16:43
 */
public class RunOutOfStaminaException extends RuntimeException {

    public RunOutOfStaminaException(String message) {
        super(message);
    }

    public RunOutOfStaminaException() {
        super("体力不足");
    }

}
