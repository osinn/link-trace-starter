package com.gitee.osinn.link.trace.thread;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Spring 线程池配置参数
 *
 * @author wency_cai
 */
@Getter
@Setter
@ConfigurationProperties(prefix = TraceTaskExecutionProperties.PREFIX)
public class TraceTaskExecutionProperties {

    public static final String PREFIX = "trace.task-execution.pool";

    private int corePoolSize = 50;
    /**
     * 核心线程池大小
     * 当线程数 >= corePoolSize，且任务队列已满时。线程池会创建新线程来处理任务
     * 当线程数 = maxPoolSize，且任务队列已满时，线程池会拒绝处理任务而抛出异常
     */
    private int maxPoolSize = 200;
    /**
     * 任务队列容量
     * 当核心线程数达到最大时，新任务会放在队列中排队等待执行
     */
    private int queueCapacity = 1000;
    /**
     * 活跃时间。单位秒
     */
    private int keepAliveSeconds = 300;

    /**
     * 允许核心线程超时
     */
    private boolean allowCoreThreadTimeout = true;

    /**
     * 线程名字前缀
     */
    private String threadNamePrefix = "my-thread-";

}
