package akarin.bot.louise2.domain.gakumas.idols;

import akarin.bot.louise2.domain.gakumas.ShowcaseContext;
import akarin.bot.louise2.domain.gakumas.effct.Effect;
import akarin.bot.louise2.domain.gakumas.effct.EffectInterface;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author akarin
 * @version 1.0
 * @description 偶像基础类
 * @date 2025/7/3 15:58
 */
@Data
public class BaseIdol {

    public enum IdolType {
        LOGICAL,
        SENSE,
        ANOMALY
    }

    private String id;

    private String name;

    private IdolType type;

    private String rarity;

    // 当前体力
    private Integer stamina;

    // 最大体力
    private Integer maxStamina;

    // 固有技能
    private Effect inherentSkill = new Effect();

    // 歌唱力
    private Double vocal;

    // 歌唱回合得分倍率
    private Double vocalRate;

    // 舞蹈力
    private Double dance;

    // 舞蹈回合得分倍率
    private Double danceRate;

    // 表现力
    private Double visual;

    // 表现回合得分倍率
    private Double visualRate;

}
