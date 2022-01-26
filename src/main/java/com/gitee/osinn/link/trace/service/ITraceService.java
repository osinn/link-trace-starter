package com.gitee.osinn.link.trace.service;

/**
 * Trace 服务接口
 */
public interface ITraceService {

    /**
     * 生成 traceId
     *
     * @return 返回 traceId
     */
    String generateTraceId();
}
