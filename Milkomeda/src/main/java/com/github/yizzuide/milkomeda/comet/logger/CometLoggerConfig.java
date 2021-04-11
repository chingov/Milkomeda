package com.github.yizzuide.milkomeda.comet.logger;

import com.github.yizzuide.milkomeda.comet.core.CometConfig;
import com.github.yizzuide.milkomeda.comet.core.CometHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * CometLoggerConfig
 *
 * @author yizzuide
 * @since 3.0.0
 * @version 3.11.0
 * Create at 2020/04/05 18:50
 */
@Configuration
@AutoConfigureAfter(CometConfig.class)
@EnableConfigurationProperties(CometLoggerProperties.class)
@ConditionalOnProperty(prefix = "milkomeda.comet.logger", name = "enable", havingValue = "true")
public class CometLoggerConfig {

    @Autowired
    public void config(CometLoggerProperties props) {
        CometHolder.setLogProps(props);
    }
}
