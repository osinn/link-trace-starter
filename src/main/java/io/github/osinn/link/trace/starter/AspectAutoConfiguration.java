package io.github.osinn.link.trace.starter;

import io.github.osinn.link.trace.TraceProperties;
import io.github.osinn.link.trace.aspect.HandlerTraceIdAspect;
import io.github.osinn.link.trace.constant.TraceConstant;
import io.github.osinn.link.trace.scheduling.TraceSchedulingProperties;
import io.github.osinn.link.trace.scheduling.aspect.HandlerSchedulingTraceIdAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AOP 自动配置
 *
 * @author wency_cai
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = "org.aspectj.lang.ProceedingJoinPoint")
@EnableConfigurationProperties(TraceSchedulingProperties.class)
@ConditionalOnProperty(value = TraceProperties.PREFIX + TraceConstant.ENABLED, havingValue = "true")
public class AspectAutoConfiguration {

    /**
     * scheduled 定时任务 AOP 自动配置
     */
    @Bean
    @ConditionalOnProperty(prefix = "trace.scheduled", value = "enabled", matchIfMissing = true)
    public HandlerSchedulingTraceIdAspect handlerSchedulingTraceIdAspect() {
        return new HandlerSchedulingTraceIdAspect();
    }

    /**
     * 基于 @TraceIdAspect 注解 AOP 自动配置
     */
    @Bean
    @ConditionalOnProperty(prefix = TraceProperties.PREFIX, value = TraceConstant.INJECTION_AOP, matchIfMissing = true)
    public HandlerTraceIdAspect handlerTraceIdAspect() {
        return new HandlerTraceIdAspect();
    }

}
