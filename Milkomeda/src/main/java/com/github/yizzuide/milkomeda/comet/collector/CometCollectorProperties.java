package com.github.yizzuide.milkomeda.comet.collector;

import com.github.yizzuide.milkomeda.comet.core.CometProperties;
import com.github.yizzuide.milkomeda.comet.core.WebCometData;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CometCollectorProperties
 *
 * @author yizzuide
 * @since 3.0.0
 * Create at 2020/03/28 21:28
 */
@Data
@ConfigurationProperties("milkomeda.comet.collector")
public class CometCollectorProperties {

    /**
     * 启用日志收集器
     */
    private boolean enable = false;

    /**
     * 开启URL标签日志收集，通过注入HttpServletResponse直接写出流需要开启 {@link CometProperties#isEnableReadResponseBody()}
     */
    private boolean enableTag = false;

    /**
     * 标签集合
     */
    private Map<String, Tag> tags = new HashMap<>();

    @Data
    public static class Tag {
        /**
         * CometData原型类
         */
        private Class<? extends WebCometData> prototype = WebCometData.class;

        /**
         * 匹配包含的路径
         */
        private List<String> include;

        /**
         * 排除路径
         */
        private List<String> exclude;


        /**
         * 异常监控器（由于异常可能被 @ControllerAdvice 吞没）
         */
        private Map<String, Object> exceptionMonitor;
    }
}
