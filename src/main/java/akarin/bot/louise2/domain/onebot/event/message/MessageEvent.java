package akarin.bot.louise2.domain.onebot.event.message;

import akarin.bot.louise2.domain.common.LouiseContext;
import akarin.bot.louise2.domain.onebot.event.PostEvent;
import akarin.bot.louise2.domain.onebot.model.api.Message;
import akarin.bot.louise2.domain.onebot.model.message.Sender;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author akarin
 * @version 1.0
 * @description 消息事件
 * @date 2025/2/13 17:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MessageEvent extends PostEvent {
    // 消息类型
    @JsonProperty("message_type")
    private String messageType;
    // 消息子类型, 如果是好友则是 friend, 如果是群临时会话则是 group, 如果是在群中自身发送则是 group_self
    @JsonProperty("sub_type")
    private String subType;
    // 消息 ID
    @JsonProperty("message_id")
    private Long messageId;
    // 发送者 QQ 号
    @JsonProperty("user_id")
    private Long userId;
    // 数组类型消息
    private ArrayList<Message> message = new ArrayList<>();
    // 原始消息内容
    @JsonProperty("raw_message")
    private String rawMessage;
    // 字体
    private Integer font;
    // 发送人信息
    private Sender sender;

    @Getter(value = AccessLevel.NONE)
    private LouiseContext context;

    public void reply(Message message) {
        Optional.ofNullable(context).orElseThrow(() -> new IllegalStateException("该消息未注入上下文"));
//        context.reply(message);
    }

}
