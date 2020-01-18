package com.github.yizzuide.milkomeda.ice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * IceServerConfig
 *
 * @author yizzuide
 * @since 1.15.2
 * @version 2.0.0
 * Create at 2019/11/21 11:14
 */
@Configuration
public class IceServerConfig extends IceBasicConfig {
    @Bean
    public DelayTimer delayTimer() {
        return new DelayTimer();
    }
}
