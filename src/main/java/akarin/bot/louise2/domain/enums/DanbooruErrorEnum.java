package akarin.bot.louise2.domain.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author akarin
 * @version 1.0
 * @description Danbooru 错误枚举类
 * @date 2025/3/1 16:58
 */
public enum DanbooruErrorEnum {

    BOORU_OK("请求成功", 200),
    BOORU_NO_CONTENT("请求成功", 204),
    BOORU_BAD_REQUEST("请求参数解析错误", 400),
    BOORU_UNAUTHORIZED("认证失败", 401),
    BOORU_FORBIDDEN("访问被拒绝", 403),
    BOORU_NOT_FOUND("资源不存在", 404),
    BOORU_GONE("超出分页限制", 410),
    BOORU_INVALID_RECORD("记录无法保存", 420),
    BOORU_LOCKED("资源被锁定，无法修改", 422),
    BOORU_ALREADY_EXISTS("资源已存在", 423),
    BOORU_INVALID_PARAMETERS("请求参数无效", 424),
    BOORU_USER_THROTTLED("用户被限流，请稍后再试", 429),
    BOORU_INTERNAL_SERVER_ERROR("数据库超时或未知服务端错误", 500),
    BOORU_BAD_GATEWAY("服务器无法处理请求，请重试", 502),
    BOORU_SERVICE_UNAVAILABLE("服务不可用，请重试", 503);

    final String name;
    final Integer value;

    private static final Map<Integer, DanbooruErrorEnum> valueMap = new HashMap<>();

    static {
        for (DanbooruErrorEnum e : values())
            valueMap.put(e.value, e);
    }

    DanbooruErrorEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public static DanbooruErrorEnum fromValue(Integer value) {
        return valueMap.get(value);
    }
}
