package akarin.bot.louise2.domain.onebot.model.message;

import akarin.bot.louise2.domain.onebot.model.api.Message;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


/**
 * @author akarin
 * @version 1.0
 * @description 合并转发类节点消息
 * @date 2025/2/13 17:24
 */
@Data
@Accessors(chain = true)
@Component
public class NodeMessage implements Message {

    @Override
    public List<MessageSegmentInterface> getMessages() {
        return new ArrayList<>(this.messages);
    }

    @Override
    public List<Node> toJSON() {
        return messages;
    }

    private List<Node> messages = new ArrayList<>();

    public NodeMessage(Long userId, String nickname, Consumer<NodeData> consumer) {
        node(userId, nickname, consumer);
    }


    public static NodeMessage init(Long userId, String nickname, Consumer<NodeData> consumer) {
        return new NodeMessage(userId, nickname, consumer);
    }


    public static NodeMessage init(Consumer<NodeData> consumer) {
        return new NodeMessage(0L, "somebody", consumer);
    }

    public NodeMessage node(Long userId, String nickname, Consumer<NodeData> consumer) {
        NodeData node = new NodeData(userId, nickname);
        consumer.accept(node);
        messages.add(new Node().setType("node").setData(node));
        return this;
    }

    public NodeMessage node(Consumer<NodeData> consumer) {
        NodeData node = new NodeData(0L, "somebody");
        consumer.accept(node);
        messages.add(new Node().setType("node").setData(node));
        return this;
    }

}
