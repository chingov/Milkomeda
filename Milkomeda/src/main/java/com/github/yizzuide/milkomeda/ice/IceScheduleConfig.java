package com.github.yizzuide.milkomeda.ice;

import com.github.yizzuide.milkomeda.universe.metadata.HandlerMetaData;
import com.github.yizzuide.milkomeda.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * IceScheduleConfig
 *
 * @author yizzuide
 * @since 1.15.0
 * @version 2.1.0
 * Create at 2019/11/17 17:00
 */
@Slf4j
@Configuration
@ConditionalOnMissingBean(SchedulingConfigurer.class)
@ConditionalOnProperty(prefix = "milkomeda.ice", name = "enable-task", havingValue = "true")
public class IceScheduleConfig {

    @Autowired
    @SuppressWarnings({"unchecked", "SpringJavaInjectionPointsAutowiringInspection", "rawtypes"})
    public void config(Ice ice, IceProperties props, ThreadPoolTaskScheduler taskScheduler) {
        taskScheduler.scheduleAtFixedRate(() -> IceContext.getTopicMap().keySet().forEach(topic -> {
            List<Job<Map<String, Object>>> jobs = ice.pop(topic, props.getTaskTopicPopMaxSize());
            if (CollectionUtils.isEmpty(jobs)) return;

            List<HandlerMetaData> metaDataList = IceContext.getTopicMap().get(topic);
            for (HandlerMetaData metaData : metaDataList) {
                try {
                    Method method = metaData.getMethod();
                    List<Job> jobList = (List) jobs;
                    ReflectUtil.invokeWithWrapperInject(metaData.getTarget(), method, jobList, Job.class, Job::getBody, Job::setBody);
                } catch (Exception e) {
                    log.error("Ice schedule error: {}", e.getMessage(), e);
                    return;
                }
            }
            // 标记完成，清除元数据
            ice.finish(jobs);
        }), Duration.ofMillis(props.getTaskExecuteRate()));
    }
}
