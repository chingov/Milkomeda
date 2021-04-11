package com.github.yizzuide.milkomeda.sundial;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 自定义数据源配置
 * @author jsq 786063250@qq.com
 * @since 3.4.0
 * @version 3.8.0
 * Create at 2020/5/8
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@Import(DelegatingBeanDefinitionRegistrar.class)
@EnableConfigurationProperties(SundialProperties.class)
@AutoConfigureBefore({DataSourceAutoConfiguration.class, TransactionAutoConfiguration.class})
public class SundialConfig {

    @Autowired
    private SundialProperties props;

    @Bean
    public DataSourceAspect dataSourceAspect(){
        return new DataSourceAspect();
    }

    @Bean
    public DataSourceFactory dataSourceFactory(){
        return new DataSourceFactory();
    }

    @ConditionalOnMissingBean
    @Bean(ShardingFunction.BEAN_ID)
    @ConditionalOnProperty(prefix = SundialProperties.PREFIX, name = "enable-sharding", havingValue = "true")
    public ShardingFunction shardingFunction() {
        return new ShardingFunction();
    }

    @ConditionalOnProperty(prefix = SundialProperties.PREFIX, name = "enable-sharding", havingValue = "true")
    @Bean
    public SundialInterceptor sundialInterceptor() {
        return new SundialInterceptor();
    }

    @Primary
    @Bean
    public DataSource dynamicRouteDataSource(DataSourceFactory dataSourceFactory) {
        DataSource defaultDataSource = null;
        Map<Object, Object> targetDataSources = new HashMap<>();
        try {
            defaultDataSource = dataSourceFactory.createDataSource();
            // 添加主数据源
            targetDataSources.put(DynamicRouteDataSource.MASTER_KEY, defaultDataSource);
            Set<Map.Entry<String, SundialProperties.Datasource>> datasourceConfigSet = props.getInstances().entrySet();
            if (CollectionUtils.isEmpty(datasourceConfigSet)) {
                return new DynamicRouteDataSource(defaultDataSource, targetDataSources);
            }
            // 不回其它数据源节点
            for (Map.Entry<String, SundialProperties.Datasource> entry : datasourceConfigSet) {
                DataSource dataSource = dataSourceFactory.createDataSource(entry.getValue());
                targetDataSources.put(entry.getKey(), dataSource);
            }
        } catch (Exception e) {
            log.error("Sundial load datasource instances error with msg: {}", e.getMessage(), e);
        }
        return new DynamicRouteDataSource(defaultDataSource, targetDataSources);
    }
}
