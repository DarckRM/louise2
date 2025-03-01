package akarin.bot.louise2;

import akarin.bot.louise2.domain.enums.DanbooruErrorEnum;
import akarin.bot.louise2.utils.HttpClientUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class Louise2ApplicationTests {

    @Test
    void contextLoads() {
        String url = "https://yande.re/post/popular_by_day.json";
        HttpClientUtil client = HttpClientUtil.builder();
        String result = client.get(url).sync();
        Response resp = client.resp();
        switch (DanbooruErrorEnum.fromValue(resp.code())) {
            case DanbooruErrorEnum.BOORU_OK: log.info("请求成功"); break;
            case DanbooruErrorEnum.BOORU_BAD_REQUEST: log.info("请求失败"); break;
            default: log.info("未知错误"); break;
        }
        JSONArray json = JSONArray.parse(result);
        log.info(json.toString());
    }

}
