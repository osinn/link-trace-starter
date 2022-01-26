package com.gitee.osinn.link.trace.utils;

import com.gitee.osinn.link.trace.constant.TraceConstant;
import com.gitee.osinn.link.trace.service.ITraceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * traceId MDC 工具类
 *
 * @author wency_cai
 */
@Slf4j
@Component
public class ThreadMdcUtil {

    private static ITraceService iTraceService;

    public ThreadMdcUtil(ITraceService iTraceService) {
        ThreadMdcUtil.iTraceService = iTraceService;
    }

    public static String put() {
        String traceId = MDC.get(TraceConstant.TRACE_ID_MDC_FIELD);
        if (StringUtils.isEmpty(traceId)) {
            traceId = iTraceService.generateTraceId();
        }
        MDC.put(TraceConstant.TRACE_ID_MDC_FIELD, traceId);
        return traceId;
    }

    public static String put(HttpServletRequest request) {
        String traceId = request.getHeader(TraceConstant.TRACE_ID_MDC_FIELD);
        if (StringUtils.isEmpty(traceId)) {
            traceId = iTraceService.generateTraceId();
        }
        MDC.put(TraceConstant.TRACE_ID_MDC_FIELD, traceId);
        return traceId;
    }

    public static String put(Map<String, Object> map) {
        String traceId = (String) map.get(TraceConstant.TRACE_ID_MDC_FIELD);
        if (StringUtils.isEmpty(traceId)) {
            traceId = iTraceService.generateTraceId();
        }
        MDC.put(TraceConstant.TRACE_ID_MDC_FIELD, traceId);
        return traceId;
    }

    public static String getTraceId() {
        return MDC.get(TraceConstant.TRACE_ID_MDC_FIELD);
    }

    public static void clear() {
        MDC.remove(TraceConstant.TRACE_ID_MDC_FIELD);
    }
}
