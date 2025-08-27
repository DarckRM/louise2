package akarin.bot.louise2.domain.gakumas.cards;

import akarin.bot.louise2.domain.gakumas.custom.Customization;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author akarin
 * @version 1.0
 * @description 附魔
 * @date 2025/7/21 17:35
 */
@Data
public class Customize<T extends Card> {

    public enum CustomType {
        NUMERIC_ENHANCE,
        ADDITION_EFFECT,
        CONTINUOS_EFFECT
    }

    // 最大附魔次数
    private Integer maxCustomCount;

    // 附魔次数
    private Integer customCount;

    // 附魔类型
    private Integer customType;

    // 附魔列表
    private Map<CustomType, List<Customization<T>>> customMap = new HashMap<>();
}
