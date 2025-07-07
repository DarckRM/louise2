package akarin.bot.louise2.domain.gakumas.effct.talent;

import akarin.bot.louise2.domain.gakumas.effct.Effect;
import lombok.Data;

/**
 * @author akarin
 * @version 1.0
 * @description 干劲: 触发增加元气值的效果时会加上干劲值
 * @date 2025/7/7 17:25
 */
@Data
public class Yaruki implements Talent {

    private Integer value;

    private Effect effect = new Effect("干劲");

    public Yaruki() {
    }

    public void increase(Integer amount) {
        this.value += amount;
    }

    public void decrease(Integer amount) {
        this.value -= amount;
    }

}
