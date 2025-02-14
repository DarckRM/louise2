package akarin.bot.louise2.domain.onebot.model.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author akarin
 * @version 1.0
 * @description TODO))
 * @date 2025/2/13 17:26
 */

@Data
public class Sender {
    @JsonProperty("user_id")
    private Long userId;
    private String nickname;
    private String sex;
    private Integer age;

    public Sender() {
    }

    public Sender(Long userId, String nickname, String sex, Integer age) {
        this.userId = userId;
        this.nickname = nickname;
        this.sex = sex;
        this.age = age;
    }
}
