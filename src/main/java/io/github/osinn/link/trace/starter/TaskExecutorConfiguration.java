package io.github.osinn.link.trace.starter;

import io.github.osinn.link.trace.thread.MdcTaskDecorator;
import io.github.osinn.link.trace.thread.TraceTaskExecutionProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 配置Spring 线程池taskExecutor
 *
 * @author wency_cai
 */
@Configuration
@EnableConfigurationProperties(TraceTaskExecutionProperties.class)
public class TaskExecutorConfiguration {

    @Bean(name = "taskExecutor")
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

}
