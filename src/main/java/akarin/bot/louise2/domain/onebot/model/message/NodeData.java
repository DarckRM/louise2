package akarin.bot.louise2.domain.onebot.model.message;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author akarin
 * @version 1.0
 * @description 转发消息
 * @date 2025/2/13 17:25
 */
@Data
public class NodeData {

    private Long userId = null;

    private String nickname = null;

    private List<MessageSegment> content = new ArrayList<>();

    public NodeData(Long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }

    public NodeData text(String text) {
        content.add(MessageSegment.text(text));
        return this;
    }

    public NodeData image(String image) {
        content.add(MessageSegment.image(image));
        return this;
    }
}
