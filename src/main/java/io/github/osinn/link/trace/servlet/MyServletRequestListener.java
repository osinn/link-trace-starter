package io.github.osinn.link.trace.servlet;

import io.github.osinn.link.trace.utils.ThreadMdcUtil;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

/**
 * 监听请求
 *
 * @author wency_cai
 */
public class MyServletRequestListener implements ServletRequestListener {

    @Override
    public void requestInitialized(ServletRequestEvent requestEvent) {
        HttpServletRequest request = (HttpServletRequest) requestEvent.getServletRequest();
        ThreadMdcUtil.put(request);
    }

    @Override
    public void requestDestroyed(ServletRequestEvent requestEvent) {
        ThreadMdcUtil.clear();
    }
}