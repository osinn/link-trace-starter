package com.gitee.osinn.link.trace.starter;

import com.gitee.osinn.link.trace.TraceProperties;
import com.gitee.osinn.link.trace.constant.TraceConstant;
import com.gitee.osinn.link.trace.service.impl.TraceServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 自动配置
 *
 * @author wency_cai
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(TraceProperties.class)
@ConditionalOnProperty(value = TraceProperties.PREFIX + TraceConstant.ENABLED, havingValue = "true")
public class LinkTraceAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public TraceServiceImpl traceServiceImpl() {
        return new TraceServiceImpl();
    }


}
