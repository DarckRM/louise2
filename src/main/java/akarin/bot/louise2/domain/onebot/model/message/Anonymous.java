package akarin.bot.louise2.domain.onebot.model.message;

import lombok.Data;

/**
 * @author akarin
 * @version 1.0
 * @description 匿名消息
 * @date 2025/2/13 18:21
 */
@Data
public class Anonymous {
    private Long id;
    private String name;
    // 匿名用户 flag, 在调用禁言 API 时需传入
    private String flag;
}
