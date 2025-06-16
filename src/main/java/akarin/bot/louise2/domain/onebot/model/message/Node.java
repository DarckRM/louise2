package akarin.bot.louise2.domain.onebot.model.message;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author akarin
 * @version 1.0
 * @description 转发类消息节点
 * @date 2025/6/13 10:40
 */
@Data
@Accessors(chain = true)
public class Node implements MessageSegmentInterface {

    private String type;

    private NodeData data;

}
