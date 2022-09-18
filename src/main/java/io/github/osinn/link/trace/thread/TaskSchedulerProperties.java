package io.github.osinn.link.trace.thread;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 任务调度 线程池配置参数
 *
 * @author wency_cai
 */
@Getter
@Setter
@ConfigurationProperties(prefix = TaskSchedulerProperties.PREFIX)
public class TaskSchedulerProperties {

    public static final String PREFIX = "trace.task-scheduler.pool";

    private int corePoolSize = 50;
}
