package io.github.osinn.link.trace.mq;

import io.github.osinn.link.trace.constant.TraceConstant;
import io.github.osinn.link.trace.utils.ThreadMdcUtil;
import io.github.osinn.link.trace.utils.TraceStrUtils;
import org.apache.rocketmq.client.hook.SendMessageContext;
import org.apache.rocketmq.client.hook.SendMessageHook;

/**
 * Rocket MQ 添加traceId链路ID
 *
 * @author wency_cai
 */
public class RocketmqProducerRequestIdHook implements SendMessageHook {

    @Override
    public String hookName() {
        return "ProduceTraceIdHook";
    }

    @Override
    public void sendMessageBefore(SendMessageContext context) {
        String traceId = ThreadMdcUtil.getTraceId();
        if (!TraceStrUtils.isEmpty(traceId)) {
            context.getMessage().putUserProperty(TraceConstant.TRACE_ID_MDC_FIELD, traceId);
        }

    }

    @Override
    public void sendMessageAfter(SendMessageContext context) {

    }
}