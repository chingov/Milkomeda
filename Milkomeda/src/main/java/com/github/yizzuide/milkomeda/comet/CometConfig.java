package com.github.yizzuide.milkomeda.comet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * CometConfig
 *
 * @author yizzuide
 * @since 2.0.0
 * Create at 2019/12/12 18:10
 */
@EnableConfigurationProperties(CometProperties.class)
@Configuration
public class CometConfig {

    @Bean
    public CometAspect cometAspect() {
        return new CometAspect();
    }

    @Bean
    @ConditionalOnProperty(prefix = "milkomeda.comet", name = "enable-read-request-body", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<CometRequestFilter> cometRequestFilter() {
        FilterRegistrationBean<CometRequestFilter> cometRequestFilter = new FilterRegistrationBean<>();
        cometRequestFilter.setFilter(new CometRequestFilter());
        cometRequestFilter.setName("cometRequestFilter");
        cometRequestFilter.setUrlPatterns(Collections.singleton("/*"));
        return cometRequestFilter;
    }

    // 配置RequestMappingHandlerAdapter实现动态注解SpringMVC处理器组件
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public void configParamResolve(RequestMappingHandlerAdapter adapter) {
        List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();
        // 动态添加针对注解 @CometParam 处理的解析器
        argumentResolvers.add(new CometParamResolver());
        argumentResolvers.addAll(Objects.requireNonNull(adapter.getArgumentResolvers()));
        adapter.setArgumentResolvers(argumentResolvers);
    }
}
