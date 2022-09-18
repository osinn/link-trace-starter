package io.github.osinn.link.trace.mq;

import io.github.osinn.link.trace.constant.TraceConstant;
import io.github.osinn.link.trace.utils.ThreadMdcUtil;
import io.github.osinn.link.trace.utils.TraceStrUtils;
import org.apache.rocketmq.client.hook.ConsumeMessageContext;
import org.apache.rocketmq.client.hook.ConsumeMessageHook;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * Rocket MQ 消费端获取traceId链路ID
 *
 * @author wency_cai
 */
public class RocketmqConsumerRequestIdtHook implements ConsumeMessageHook {
    @Override
    public String hookName() {
        return "ConsumerTraceIdtHook";
    }

    @Override
    public void consumeMessageBefore(ConsumeMessageContext context) {
        for (MessageExt messageExt : context.getMsgList()) {
            String traceId = messageExt.getUserProperty(TraceConstant.TRACE_ID_MDC_FIELD);
            if (TraceStrUtils.isEmpty(traceId)) {
                ThreadMdcUtil.put();
            } else {
                ThreadMdcUtil.put(traceId);
            }
        }
    }

    @Override
    public void consumeMessageAfter(ConsumeMessageContext context) {

    }
}