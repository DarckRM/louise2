package akarin.bot.louise2.domain.onebot.model.message;

import akarin.bot.louise2.domain.enums.onebot.MessageSegmentEnum;
import akarin.bot.louise2.domain.onebot.model.api.Message;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author akarin
 * @version 1.0
 * @description 数组类型消息
 * @date 2025/2/13 18:22
 */
@Getter
public class ArrayMessage implements Message {

    private final List<MessageSegment> messages = new ArrayList<>();

    public ArrayMessage text(String text) {
        MessageSegment segment = MessageSegment.text(text);
        messages.add(segment);
        return this;
    }

    public ArrayMessage image(String image) {
        MessageSegment segment = MessageSegment.image(image);
        messages.add(segment);
        return this;
    }

    public ArrayMessage image(String image, String type) {
        MessageSegment segment;
        if ("file".equals(type))
            segment = MessageSegment.image("file://" + image);
        else
            segment = MessageSegment.image(image);
        messages.add(segment);
        return this;
    }

    @Override
    public String toJSON() {
        return JSONObject.toJSONString(messages);
    }
}
