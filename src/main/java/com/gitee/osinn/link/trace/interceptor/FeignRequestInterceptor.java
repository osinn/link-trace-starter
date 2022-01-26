package com.gitee.osinn.link.trace.interceptor;

import com.gitee.osinn.link.trace.constant.TraceConstant;
import com.gitee.osinn.link.trace.utils.ThreadMdcUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * feign请求拦截器
 *
 * @author wency_cai
 */
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String traceId = ThreadMdcUtil.put();
        template.header(TraceConstant.TRACE_ID_MDC_FIELD, traceId);
    }

}
