package com.gitee.osinn.link.trace.interceptor;

import com.gitee.osinn.link.trace.utils.ThreadMdcUtil;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;


/**
 * feign开启Hystrix 需要 配置Hystrix线程池隔离支持日志链路跟踪
 *
 * @author wency_cai
 */
public class MdcHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {

    public MdcHystrixConcurrencyStrategy() {
        HystrixPlugins.reset();
        HystrixPlugins.getInstance().registerConcurrencyStrategy(this);
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        return new MdcAwareCallable<>(callable, MDC.getCopyOfContextMap());
    }

    private static class MdcAwareCallable<T> implements Callable<T> {

        private final Callable<T> delegate;

        private final Map<String, String> contextMap;

        public MdcAwareCallable(Callable<T> callable, Map<String, String> contextMap) {
            this.delegate = callable;
            this.contextMap = contextMap != null ? contextMap : new HashMap<>();
        }

        @Override
        public T call() throws Exception {
            try {
                MDC.setContextMap(contextMap);
                return delegate.call();
            } finally {
                ThreadMdcUtil.clear();
            }
        }
    }
}
