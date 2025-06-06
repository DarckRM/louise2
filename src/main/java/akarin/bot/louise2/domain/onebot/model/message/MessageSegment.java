package akarin.bot.louise2.domain.onebot.model.message;

import akarin.bot.louise2.domain.enums.onebot.MessageSegmentEnum;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author akarin
 * @version 1.0
 * @description 消息段
 * @date 2025/6/6 14:23
 */
@Accessors(chain = true)
@Data
public class MessageSegment {

    @JsonIgnore
    private MessageSegmentEnum typeEnum;

    private JSONObject data;

    private String type;

    public MessageSegment() {
    }

    public MessageSegment(MessageSegmentEnum type, JSONObject data) {
        this.typeEnum = type;
        this.type = type.getType();
        this.data = data;
    }

    public static MessageSegment text(String text) {
        return new MessageSegment(MessageSegmentEnum.TEXT, new JSONObject().fluentPut(MessageSegmentEnum.TEXT.getType(),
                text));
    }

    public static MessageSegment image(String image) {
        return new MessageSegment(MessageSegmentEnum.IMAGE,
                new JSONObject().fluentPut(MessageSegmentEnum.FILE.getType(), image));
    }

}
