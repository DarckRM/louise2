package akarin.bot.louise2.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author akarin
 * @version 1.0
 * @description HttpClient 工具类
 * @date 2025/3/1 17:10
 */
@Slf4j
public class HttpClientUtil {

    private final OkHttpClient client;

    private final Map<String, String> headers;
    private final Map<String, Object> params;
    private String body;
    private String url;
    private Request.Builder request;
    private Response response;

    private HttpClientUtil() {
        headers = new HashMap<>();
        params = new HashMap<>();
        TrustManager[] managers = buildTrustManagers();
        client = new OkHttpClient.Builder()
                .connectTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .sslSocketFactory(createSSLSocketFactory(managers), (X509TrustManager) managers[0])
                .hostnameVerifier((hostname, session) -> true)
                .retryOnConnectionFailure(true)
                .build();
        addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like " +
                "Gecko) Chrome/63.0.3239.132 Safari/537.36");
    }

    public static HttpClientUtil builder() {
        return new HttpClientUtil();
    }

    public HttpClientUtil url(String url) {
        this.url = url;
        return this;
    }

    public HttpClientUtil addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public HttpClientUtil addParam(String key, String value) {
        params.put(key, value);
        return this;
    }

    public HttpClientUtil addBody(String value) {
        body = value;
        return this;
    }

    private String encodeUTF8(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    public HttpClientUtil get(String url) {
        StringBuilder builder = new StringBuilder(url);
        if (!params.isEmpty())
            builder.append("?");
        params.forEach((k, v) -> builder.append("&").append(encodeUTF8(k)).append("=").append(encodeUTF8((String) v)));
        if (!params.isEmpty())
            builder.deleteCharAt(builder.length() - 1);
        this.url = builder.toString();
        request = new Request.Builder().get();
        return this;
    }

    public HttpClientUtil post(String url) {
        this.url = url;
        RequestBody reqBody = RequestBody.create(body, MediaType.parse("application/json; charset=utf-8"));
        request = new Request.Builder().post(reqBody);
        return this;
    }

    private Request build() {
        headers.forEach((k, v) -> request.addHeader(k, v));
        return request.url(url).build();
    }

    public Response resp() {
        return this.response;
    }

    // 同步请求
    public String sync() {
        try {
            try (Response response = client.newCall(build()).execute()) {
                this.response = response;
                return Objects.requireNonNull(response.body()).string();
            }
        } catch (IOException e) {
            log.error("请求失败: {}", e.getMessage());
            return null;
        }
    }

    public Response syncResp() {
        try {
            return client.newCall(build()).execute();
        } catch (IOException e) {
            log.error("请求失败: ", e);
            return null;
        }
    }

    /**
     * @author akarin
     * @description 默认处理函数异步请求
     * @date 2025/3/1 17:59
     **/
    public String async() {
        StringBuilder result = new StringBuilder();
        client.newCall(build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                result.append("请求失败: ").append(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                result.append(Objects.requireNonNull(response.body()).string());
            }
        });
        return result.toString();
    }

    // 自定义处理函数异步请求
    public void async(Callback callback) {
        client.newCall(build()).enqueue(callback);
    }

    /**
     * @param trustAllCerts 参数
     * @return javax.net.ssl.SSLSocketFactory
     * @author akarin
     * @description 生成 SSLSocket 工厂
     * @date 2025/3/1 17:27
     **/
    private static SSLSocketFactory createSSLSocketFactory(TrustManager[] trustAllCerts) {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            log.error("创建 SSLSocketFactory 失败: {}", e.getMessage());
        }
        return ssfFactory;
    }

    private static TrustManager[] buildTrustManagers() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
    }
}
