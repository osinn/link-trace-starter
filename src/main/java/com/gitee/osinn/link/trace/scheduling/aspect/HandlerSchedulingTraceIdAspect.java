package com.gitee.osinn.link.trace.scheduling.aspect;

import com.gitee.osinn.link.trace.utils.ThreadMdcUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;


/**
 * scheduled 定时任务 traceId AOP
 *
 * @author wency_cai
 */
@Aspect
@Slf4j
public class HandlerSchedulingTraceIdAspect {


    @Pointcut("execution (@org.springframework.scheduling.annotation.Scheduled  * *.*(..))")
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
