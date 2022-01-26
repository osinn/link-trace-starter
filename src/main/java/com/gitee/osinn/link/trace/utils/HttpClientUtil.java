package com.gitee.osinn.link.trace.utils;

import com.gitee.osinn.link.trace.interceptor.HttpClientTraceIdInterceptor;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * HttpClient工具类
 *
 * @author wency_cai
 */
public class HttpClientUtil {

    /**
     * 添加拦截器
     * 可以自行封装HttpClient工具类，添加HttpClientTraceIdInterceptor拦截器即可
     */
    public static final CloseableHttpClient HTTP_CLIENT = HttpClientBuilder.create()
            .addInterceptorFirst(new HttpClientTraceIdInterceptor())
            .build();

    /**
     * 示例
     *
     * @param url 请求地址
     * @return 返回响应数据
     */
    public static String doGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = HTTP_CLIENT.execute(httpGet);
            HttpEntity resEntity = response.getEntity();
            return EntityUtils.toString(resEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
