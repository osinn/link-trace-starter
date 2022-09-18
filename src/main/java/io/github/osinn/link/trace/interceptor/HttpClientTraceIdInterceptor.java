package io.github.osinn.link.trace.interceptor;

import io.github.osinn.link.trace.constant.TraceConstant;
import io.github.osinn.link.trace.utils.ThreadMdcUtil;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * HttpClient请求拦截器
 *
 * @author wency_cai
 */
public class HttpClientTraceIdInterceptor implements HttpRequestInterceptor {

    @Override
    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        String traceId = ThreadMdcUtil.put();
        httpRequest.addHeader(TraceConstant.TRACE_ID_MDC_FIELD, traceId);
    }
}
