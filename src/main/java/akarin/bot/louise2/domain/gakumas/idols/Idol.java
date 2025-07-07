package akarin.bot.louise2.domain.gakumas.idols;

import akarin.bot.louise2.domain.gakumas.ShowcaseContext;
import akarin.bot.louise2.domain.gakumas.effct.Effect;
import akarin.bot.louise2.domain.gakumas.effct.talent.Talent;
import akarin.bot.louise2.domain.gakumas.exceptions.RunOutOfStaminaException;

/**
 * @author akarin
 * @version 1.0
 * @description 偶像接口
 * @date 2025/7/3 16:26
 */
public interface Idol {

    String getName();

    void init(ShowcaseContext context);

    Talent getTalent();

    Integer getGenki();

    void setGenki(Integer genki);

    Integer getStamina();

    void setStamina(Integer stamina);

    Integer getMaxStamina();

    default void directStaminaCost(Integer amount) {
        if (getStamina() - amount < 0)
            throw new RunOutOfStaminaException();
        setStamina(getStamina() - amount);
    }

    default void staminaCost(Integer amount) {
        if (getStamina() + getGenki() - amount < 0)
            throw new RunOutOfStaminaException();
        if (getGenki() > amount)
            setGenki(getGenki() - amount);
        else {
            setStamina(getStamina() - (amount - getGenki()));
            setGenki(0);
        }
    }

    default void staminaRecover(Integer amount) {
        if (getStamina() + amount > getMaxStamina()) {
            setStamina(getMaxStamina());
            return;
        }
        setStamina(getStamina() + amount);
    }

    Effect getInherentSkill();

    Double getVocalRate();

    Double getDanceRate();

    Double getVisualRate();

    String description();

}
