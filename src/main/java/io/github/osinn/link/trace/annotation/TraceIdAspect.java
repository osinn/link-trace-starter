package io.github.osinn.link.trace.annotation;

import java.lang.annotation.*;

/**
 * <pre>
 *    AOP traceId 跟踪
 *    直接在方法上添加此注解即可
 * </pre>
 *
 * @author wency_cai
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TraceIdAspect {
}
