package io.github.osinn.link.trace.scheduling;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 定时任务配置参数
 *
 * @author wency_cai
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "trace.scheduled")
public class TraceSchedulingProperties {

    /**
     * Enable tracing for {@link org.springframework.scheduling.annotation.Scheduled}.
     */
    private boolean enabled = true;

}
