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
    FACE("face", 2),
    IMAGE("image", 3),
    RECORD("record", 4),
    VIDEO("video", 5),
    AT("at", 6),
    RPS("rps", 7),
    DICE("dice", 8),
    SHAKE("shake", 9),
    POKE("poke", 10),
    ANONYMOUS("anonymous", 11),
    SHARE("share", 12),
    CONTACT("contact", 13),
    LOCATION("location", 14),
    MUSIC("location", 15),
    REPLY("reply", 16),
    FORWARD("forward", 17),
    NODE("node", 18),
    JSON("json", 19),
    XML("xml", 20);

    private final String type;

    private final Integer value;

    MessageSegmentEnum(String type, Integer value) {
        this.type = type;
        this.value = value;
    }

    @Getter
    public enum MediaType {
        FILE("file", 1);

        private final String type;
        private final Integer value;

        MediaType(String type, Integer value) {
            this.type = type;
            this.value = value;
        }
    }

    @Getter
    public enum MusicType {
        QQ("qq", 1),
        NET_EASE("163", 2),
        XIA_MI("xm", 3);

        private final String type;
        private final Integer value;

        MusicType(String type, Integer value) {
            this.type = type;
            this.value = value;
        }
    }

    @Getter
    public enum ContactType {
        USER("user", 1),
        GROUP("group", 2);

        private final String type;
        private final Integer value;

        ContactType(String type, Integer value) {
            this.type = type;
            this.value = value;
        }
    }

}

