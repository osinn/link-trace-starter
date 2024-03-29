package io.github.osinn.link.trace.starter;

import io.github.osinn.link.trace.TraceProperties;
import io.github.osinn.link.trace.constant.TraceConstant;
import io.github.osinn.link.trace.mq.RocketMqTrace;
import io.github.osinn.link.trace.service.impl.TraceServiceImpl;
import io.github.osinn.link.trace.utils.ThreadMdcUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * 自动配置
 *
 * @author wency_cai
 */
@Slf4j
@Configuration
@Import(ThreadMdcUtil.class)
@EnableConfigurationProperties(TraceProperties.class)
@ConditionalOnProperty(value = TraceProperties.PREFIX + TraceConstant.ENABLED, havingValue = "true")
public class LinkTraceAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public TraceServiceImpl traceServiceImpl() {
        return new TraceServiceImpl();
    }

    @Bean
    @ConditionalOnProperty(prefix = TraceProperties.PREFIX, value = TraceConstant.ENABLED_ROCKETMQ, matchIfMissing = true)
    public RocketMqTrace rocketMqTrace() {
        return new RocketMqTrace();
    }

}
