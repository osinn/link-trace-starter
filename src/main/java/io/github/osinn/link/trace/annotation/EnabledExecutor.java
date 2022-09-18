package io.github.osinn.link.trace.annotation;

import io.github.osinn.link.trace.starter.TaskExecutorConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.annotation.*;

/**
 * 启用创建Spring 默认线程池
 *
 * @author wency_cai
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(TaskExecutorConfiguration.class)
@Documented
@EnableAsync
public @interface EnabledExecutor {
}
