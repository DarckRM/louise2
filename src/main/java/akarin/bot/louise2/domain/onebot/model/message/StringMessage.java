package akarin.bot.louise2.domain.onebot.model.message;

import akarin.bot.louise2.domain.onebot.model.api.Message;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
* @description 字符串类型消息
* @author akarin
* @date 2025/2/13 18:27
* @version 1.0
*/
@Data
@Getter
public class StringMessage implements Message {

    private final List<MessageSegmentInterface> messages;

    @Override
    public String toJSON() {
        return null;
    }
}
