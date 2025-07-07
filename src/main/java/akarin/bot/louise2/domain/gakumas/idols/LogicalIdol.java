package akarin.bot.louise2.domain.gakumas.idols;

import akarin.bot.louise2.domain.gakumas.effct.talent.NiceExperience;
import akarin.bot.louise2.domain.gakumas.effct.talent.Talent;
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
    private NiceExperience niceExperience;
}
