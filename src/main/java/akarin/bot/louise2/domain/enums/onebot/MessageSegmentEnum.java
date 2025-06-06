package akarin.bot.louise2.domain.enums.onebot;
import lombok.Getter;

/**
 * @author akarin
 * @version 1.0
 * @description 消息段枚举类型
 * @date 2025/6/6 14:30
 */
@Getter
public enum MessageSegmentEnum {

    TEXT("text", 1),
    IMAGE("image", 2),
    MUSIC("music", 3),
    VIDEO("video", 4),
    FACE("video", 4),
    FILE("file", 5);

    private final String type;

    private final Integer value;

    MessageSegmentEnum(String type, Integer value) {
        this.type = type;
        this.value = value;
    }

}
