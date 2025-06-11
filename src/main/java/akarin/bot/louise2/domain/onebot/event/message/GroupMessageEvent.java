package akarin.bot.louise2.domain.onebot.event.message;

import akarin.bot.louise2.domain.onebot.model.message.Anonymous;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author akarin
 * @version 1.0
 * @description 群聊消息事件
 * @date 2025/2/13 18:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupMessageEvent extends MessageEvent {

    // 发送群 QQ 号
    @JsonProperty("group_id")
    private Long groupId;

    // 临时会话来源
    @JsonProperty("temp_source")
    private String tempSource;

    // 匿名信息, 如果不是匿名消息则为 null
    private Anonymous anonymous;
}
