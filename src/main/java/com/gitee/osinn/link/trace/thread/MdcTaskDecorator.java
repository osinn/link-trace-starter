package com.gitee.osinn.link.trace.thread;

import com.gitee.osinn.link.trace.utils.ThreadMdcUtil;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * 描述
 *
 * @author wency_cai
 */
public class MdcTaskDecorator implements TaskDecorator {

    /**
     * 使异步线程池获得主线程的上下文
     *
     * @param runnable
     * @return
     */
    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> context = MDC.getCopyOfContextMap();
        return () -> {
            try {
                MDC.setContextMap(context);
                runnable.run();
            } finally {
                ThreadMdcUtil.clear();
            }
        };
    }

    /**
     * 使异步线程池获得主线程的上下文
     *
     * @param runnable
     * @return
     */
    public static Runnable runnable(Runnable runnable) {
        Map<String, String> context = MDC.getCopyOfContextMap();
        return () -> {
            try {
                MDC.setContextMap(context);
                runnable.run();
            } finally {
                ThreadMdcUtil.clear();
            }
        };
    }
}

