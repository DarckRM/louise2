package akarin.bot.louise2.domain.onebot.model.message;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

import java.util.ArrayList;

/**
 * @author akarin
 * @version 1.0
 * @description TODO))
 * @date 2025/2/13 17:25
 */
@Data
public class NodeData {
    // 转发消息id
    Integer id;
    // 发送者显示名字
    String name;
    // 发送者QQ号
    Long uin;
    // 用于自定义消息 不支持转发套娃
    ArrayList<JSONObject> content = new ArrayList<>();
}
