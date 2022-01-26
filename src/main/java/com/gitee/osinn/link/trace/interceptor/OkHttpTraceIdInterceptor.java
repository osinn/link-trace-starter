package com.gitee.osinn.link.trace.interceptor;

import com.gitee.osinn.link.trace.constant.TraceConstant;
import com.gitee.osinn.link.trace.utils.ThreadMdcUtil;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * OkHttp请求拦截器
 *
 * @author wency_cai
 */
public class OkHttpTraceIdInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        String traceId = ThreadMdcUtil.put();
        Request request = chain.request().newBuilder().addHeader(TraceConstant.TRACE_ID_MDC_FIELD, traceId).build();
        return chain.proceed(request);
    }
}
