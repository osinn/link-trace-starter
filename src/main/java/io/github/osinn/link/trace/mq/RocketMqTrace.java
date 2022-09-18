package io.github.osinn.link.trace.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * 将consumer和producer的Hook注册到对应的对象中，实现链路跟踪
 *
 * @author wency_cai
 */
@Slf4j
public class RocketMqTrace implements ApplicationContextAware, CommandLineRunner {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(String... args) {
        for (String beanName : applicationContext.getBeanNamesForType(RocketMQTemplate.class)) {
            RocketMQTemplate bean = applicationContext.getBean(beanName, RocketMQTemplate.class);
            if (null != bean.getProducer()) {
                bean.getProducer().getDefaultMQProducerImpl().registerSendMessageHook(new RocketmqProducerRequestIdHook());
            }
        }
        for (String beanName : applicationContext.getBeanNamesForType(DefaultRocketMQListenerContainer.class)) {
            DefaultRocketMQListenerContainer bean = applicationContext.getBean(beanName, DefaultRocketMQListenerContainer.class);
            if (null != bean.getConsumer()) {
                bean.getConsumer().getDefaultMQPushConsumerImpl().registerConsumeMessageHook(new RocketmqConsumerRequestIdtHook());
            }
        }
    }
}