package com.gitee.osinn.link.trace.starter;

import com.gitee.osinn.link.trace.TraceProperties;
import com.gitee.osinn.link.trace.constant.TraceConstant;
import com.gitee.osinn.link.trace.thread.MdcThreadPoolTaskScheduler;
import com.gitee.osinn.link.trace.thread.TaskSchedulerProperties;
import com.gitee.osinn.link.trace.utils.ThreadMdcUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;

/**
 * 线程池配置
 * 在@EnableAsync 开启 异步或多线程下丢失 traceId 问题
 *
 * @author wency_cai
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(TaskSchedulerProperties.class)
@ConditionalOnProperty(value = TraceProperties.PREFIX + TraceConstant.ENABLED, havingValue = "true")
public class TaskSchedulerConfig {

    @Bean
    @ConditionalOnProperty(prefix = "trace.scheduled", value = "enabled", matchIfMissing = true)
    public TaskScheduler taskScheduler(TaskSchedulerProperties taskSchedulerProperties) {
        MdcThreadPoolTaskScheduler threadPoolTaskScheduler = new MdcThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(taskSchedulerProperties.getCorePoolSize());
        threadPoolTaskScheduler.setThreadNamePrefix("my-ThreadPoolTaskScheduler");
        threadPoolTaskScheduler.setErrorHandler(throwable -> {
            log.error(throwable.getMessage(), throwable);
            ThreadMdcUtil.clear();
        });
        return threadPoolTaskScheduler;
    }
}
