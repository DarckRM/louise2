package akarin.bot.louise2.domain.gakumas.idols;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author akarin
 * @version 1.0
 * @description 理性系小偶像
 * @date 2025/7/3 16:38
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LogicalIdol extends BaseIdol {

    // 好印象
    private Integer niceExperience = 0;

}
