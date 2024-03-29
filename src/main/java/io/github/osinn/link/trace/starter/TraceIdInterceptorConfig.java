package io.github.osinn.link.trace.starter;

import io.github.osinn.link.trace.TraceProperties;
import io.github.osinn.link.trace.constant.TraceConstant;
import io.github.osinn.link.trace.interceptor.FeignRequestInterceptor;
import io.github.osinn.link.trace.interceptor.MdcHystrixConcurrencyStrategy;
import io.github.osinn.link.trace.servlet.MyServletRequestListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 描述
 *
 * @author wency_cai
 */
@Configuration
@Import(TaskSchedulerConfig.class)
@ConditionalOnProperty(value = TraceProperties.PREFIX + TraceConstant.ENABLED, havingValue = "true")
public class TraceIdInterceptorConfig {

    @Bean
    public MyServletRequestListener myServletRequestListener() {
        return new MyServletRequestListener();
    }


    @Configuration
    @ConditionalOnProperty(value = TraceProperties.PREFIX + ".cloud.enabled-feign", havingValue = "true")
    public static class FeignRequest {
        @Bean
        public FeignRequestInterceptor feignRequestInterceptor() {
            return new FeignRequestInterceptor();
        }
    }

    @Configuration
    @ConditionalOnProperty(value = TraceProperties.PREFIX + ".cloud.enabled-hystrix", havingValue = "true")
    public static class MdcHystrix {

        @Bean
        public MdcHystrixConcurrencyStrategy mdcHystrixConcurrencyStrategy() {
            return new MdcHystrixConcurrencyStrategy();
        }
    }
}
