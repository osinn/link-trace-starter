package com.gitee.osinn.link.trace.aspect;

import com.gitee.osinn.link.trace.utils.ThreadMdcUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;


/**
 * traceId AOP
 *
 * @author wency_cai
 */
@Aspect
@Slf4j
public class HandlerTraceIdAspect {

    @Pointcut("execution (@com.gitee.osinn.link.trace.annotation.TraceIdAspect  * *.*(..))")
    public void traceIdAspect() {
    }

    @Before(value = "traceIdAspect()")
    public void handleTraceId(JoinPoint joinPoint) {
        ThreadMdcUtil.put();
    }

    @AfterReturning(pointcut = "traceIdAspect()", returning = "returnVal")
    public void afterReturn(JoinPoint joinPoint, Object returnVal) {
        ThreadMdcUtil.clear();
    }

}
