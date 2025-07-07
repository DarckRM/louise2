package akarin.bot.louise2.domain.gakumas.idols;

import akarin.bot.louise2.domain.gakumas.ShowcaseContext;
import akarin.bot.louise2.domain.gakumas.effct.Effect;
import akarin.bot.louise2.domain.gakumas.effct.talent.Talent;

/**
 * @author akarin
 * @version 1.0
 * @description 偶像接口
 * @date 2025/7/3 16:26
 */
public interface Idol {

    void init(ShowcaseContext context);

    Talent getTalent();

    Integer getStamina();

    void setStamina(Integer stamina);

    default void staminaCost(Integer amount) {
        setStamina(getStamina() - amount);
    }

    default void staminaRecover(Integer amount) {
        setStamina(getStamina() + amount);
    }

    Effect getInherentSkill();

    Double getVocalRate();

    Double getDanceRate();

    Double getVisualRate();

}
