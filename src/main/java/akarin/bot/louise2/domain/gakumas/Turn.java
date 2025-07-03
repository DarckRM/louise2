package akarin.bot.louise2.domain.gakumas;

import lombok.Data;
import lombok.experimental.Accessors;

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

    public Turn(TurnType type) {
        this.type = type;
    }

}
