package com.gitee.osinn.link.trace;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wency_cai
 */
@Getter
@Setter
@ConfigurationProperties(prefix = TraceProperties.PREFIX)
public class TraceProperties {

    public static final String PREFIX = "trace";

    private boolean enabled = true;

    private Cloud cloud;

    @Getter
    @Setter
    public static class Cloud {

        private boolean enabledFeign;

        private boolean enabledHystrix;
    }
}
