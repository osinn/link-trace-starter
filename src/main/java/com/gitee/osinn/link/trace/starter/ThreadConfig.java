package com.gitee.osinn.link.trace.starter;

import com.gitee.osinn.link.trace.TraceProperties;
import com.gitee.osinn.link.trace.constant.TraceConstant;
import com.gitee.osinn.link.trace.service.impl.TraceServiceImpl;
import com.gitee.osinn.link.trace.thread.MdcTaskDecorator;
import com.gitee.osinn.link.trace.thread.MdcThreadPoolTaskScheduler;
import com.gitee.osinn.link.trace.thread.TraceTaskExecutionProperties;
import com.gitee.osinn.link.trace.utils.ThreadMdcUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 * 在@EnableAsync 开启 异步或多线程下丢失 traceId 问题
 *
 * @author wency_cai
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(TraceTaskExecutionProperties.class)
@ConditionalOnProperty(value = TraceProperties.PREFIX + TraceConstant.ENABLED, havingValue = "true")
public class ThreadConfig {

    @Bean
    @ConditionalOnMissingBean
    public TraceServiceImpl traceServiceImpl() {
        return new TraceServiceImpl();
    }

    @Bean
    @ConditionalOnProperty(prefix = "trace.task-execution.pool", value = "enabled", matchIfMissing = true)
    public Executor taskExecutor(TraceTaskExecutionProperties traceTaskExecutionProperties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(traceTaskExecutionProperties.getMaxPoolSize());
        executor.setCorePoolSize(traceTaskExecutionProperties.getCorePoolSize());
        executor.setQueueCapacity(traceTaskExecutionProperties.getQueueCapacity());
        executor.setKeepAliveSeconds(traceTaskExecutionProperties.getKeepAliveSeconds());
        executor.setAllowCoreThreadTimeOut(traceTaskExecutionProperties.isAllowCoreThreadTimeout());
        // 关键
        executor.setTaskDecorator(new MdcTaskDecorator());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix(traceTaskExecutionProperties.getThreadNamePrefix());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

    @Bean
    @ConditionalOnProperty(prefix = "trace.scheduled", value = "enabled", matchIfMissing = true)
    public TaskScheduler taskScheduler(TraceTaskExecutionProperties traceTaskExecutionProperties) {
        MdcThreadPoolTaskScheduler threadPoolTaskScheduler = new MdcThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(traceTaskExecutionProperties.getCorePoolSize());
        threadPoolTaskScheduler.setThreadNamePrefix("my-ThreadPoolTaskScheduler");
        threadPoolTaskScheduler.setErrorHandler(throwable -> {
            log.error(throwable.getMessage(), throwable);
            ThreadMdcUtil.clear();
        });
        return threadPoolTaskScheduler;
    }
}
