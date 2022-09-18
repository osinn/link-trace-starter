package io.github.osinn.link.trace.utils;

import io.github.osinn.link.trace.interceptor.OkHttpTraceIdInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Objects;

/**
 * OkHttp工具类
 *
 * @author wency_cai
 */
public class OkHttpUtil {

    /**
     * 添加拦截器
     * 可以自行封装OkHttp工具类，添加OkHttpTraceIdInterceptor拦截器即可
     */
    public static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient().newBuilder()
            .addNetworkInterceptor(new OkHttpTraceIdInterceptor())
            .build();

    /**
     * 示例
     *
     * @param url 请求地址
     * @return 返回响应数据
     */
    public static String doGet(String url) {
        String result = null;
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            result = Objects.requireNonNull(response.body()).string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
