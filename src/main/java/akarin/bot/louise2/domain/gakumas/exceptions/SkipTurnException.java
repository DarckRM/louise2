package akarin.bot.louise2.domain.gakumas.exceptions;

/**
 * @author akarin
 * @version 1.0
 * @description 跳过回合
 * @date 2025/7/7 16:43
 */
public class SkipTurnException extends GakumasShowcaseException {

    public SkipTurnException(String message) {
        super(message);
    }

    public SkipTurnException() {
        super("跳过回合");
    }

}
