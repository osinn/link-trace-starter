package com.gitee.osinn.link.trace.starter;

import com.gitee.osinn.link.trace.TraceProperties;
import com.gitee.osinn.link.trace.constant.TraceConstant;
import com.gitee.osinn.link.trace.scheduling.TraceSchedulingProperties;
import com.gitee.osinn.link.trace.utils.ThreadMdcUtil;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Rabbitmq 消息队列链路跟踪配置
 *
 * @author wency_cai
 */
@Configuration
@EnableConfigurationProperties(TraceSchedulingProperties.class)
@ConditionalOnProperty(value = TraceProperties.PREFIX + TraceConstant.ENABLED_RABBITMQ, havingValue = "true")
public class RabbitmqTraceAutoConfig {

    @Bean(name = "rabbitListenerContainerFactory")
    @ConditionalOnProperty(prefix = "spring.rabbitmq.listener", name = "type", havingValue = "simple", matchIfMissing = true)
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer,
                                                                                     ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        //消息接收之前加拦截处理，每次接收消息都会调用，是有压测消息标记的，先存到副本变量，后续的操作数据库根据这个变量进行切换影子库
        factory.setAfterReceivePostProcessors(message -> {
            Map<String, Object> header = message.getMessageProperties().getHeaders();
            ThreadMdcUtil.put(header);
            return message;
        });
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory factory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        //发送之前加一个拦截器，每次发送都会调用这个方法，方法名称已经说明了一切了
        rabbitTemplate.setBeforePublishPostProcessors(message -> {
            String traceId = ThreadMdcUtil.put();
            message.getMessageProperties().getHeaders().put("traceId", traceId);
            return message;
        });
        return rabbitTemplate;
    }
}
