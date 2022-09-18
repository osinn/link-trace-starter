package io.github.osinn.link.trace.service.impl;

import io.github.osinn.link.trace.service.ITraceService;

import java.util.UUID;

/**
 * Trace 服务接口
 *
 * @author wency_cai
 */
public class TraceServiceImpl implements ITraceService {

    @Override
    public String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}
