package akarin.bot.louise2.domain.gakumas;

import akarin.bot.louise2.domain.gakumas.effct.EffectInterface;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author akarin
 * @version 1.0
 * @description 圣遗物（
 * @date 2025/7/3 16:05
 */
@Accessors(chain = true)
@Data
public class BaseArtifact {

    private Long id;

    private String name;

    private String type;

    private String rarity;

    // 圣遗物具有的效果
    private List<EffectInterface> effects = new ArrayList<>();
}
