package com.github.yizzuide.milkomeda.echo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * EchoProperties
 *
 * @author yizzuide
 * @since 1.13.3
 * @version 3.0.0
 * Create at 2019/10/23 20:53
 */
@Data
@ConfigurationProperties("milkomeda.echo")
public class EchoProperties {
    /**
     * 连接池最大连接数
     */
    private int poolMaxSize = 200;
    /**
     * 每个路由的并发量
     */
    private int defaultMaxPerRoute = 50;
    /**
     * 连接超时（单位：ms）
     */
    @DurationUnit(ChronoUnit.MILLIS)
    private Duration connectTimeout = Duration.ofMillis(5000);
    /**
     * 数据读取超时（单位：ms）
     */
    @DurationUnit(ChronoUnit.MILLIS)
    private Duration readTimeout = Duration.ofMillis(5000);
    /**
     * 从池中获取请求连接超时（单位：ms，不宜过长）
     */
    @DurationUnit(ChronoUnit.MILLIS)
    private Duration connectionRequestTimeout = Duration.ofMillis(200);
    /**
     * 缓冲请求数据，默认false，通过POST或者PUT大量发送数据时，建议不要修改，以免耗尽内存（注意：Spring boot 1.5.x及以下需要设置为true）
     */
    private boolean enableBufferRequestBody = false;
    /**
     * 连接保活时长（单位：ms）
     */
    @DurationUnit(ChronoUnit.MILLIS)
    private Duration keepAlive = Duration.ofMillis(5000);
    /**
     * 允许重试
     */
    private boolean enableRequestSentRetry = true;
    /**
     * 重试次数
     */
    private int retryCount = 3;
}
