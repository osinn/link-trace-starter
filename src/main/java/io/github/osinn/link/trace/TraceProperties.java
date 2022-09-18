package io.github.osinn.link.trace;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wency_cai
 */
@Getter
@Setter
@ConfigurationProperties(prefix = TraceProperties.PREFIX)
public class TraceProperties {

    public static final String PREFIX = "trace";

    /**
     * 开启链路跟踪
     */
    private boolean enabled = true;

    /**
     * 开启RabbitMQ链路跟踪
     */
    private boolean enabledRabbitmq;

    /**
     * 开启RocketMQ链路跟踪
     */
    private boolean enabledRocketmq;

    /**
     * 注入HandlerTraceIdAspect后可以在方法上使用@HandlerTraceIdAspect注解实现traceId链路跟踪
     */
    private boolean injectionAop;

    /**
     * 微服务链路跟踪
     */
    private Cloud cloud;

    @Getter
    @Setter
    public static class Cloud {

        /**
         * 开启Feign 链路跟踪
         */
        private boolean enabledFeign;

        /**
         * 开启Hystrix 链路跟踪
         */
        private boolean enabledHystrix;
    }
}
