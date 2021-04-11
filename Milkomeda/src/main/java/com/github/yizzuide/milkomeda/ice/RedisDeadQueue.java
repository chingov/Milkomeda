package com.github.yizzuide.milkomeda.ice;

import com.github.yizzuide.milkomeda.universe.context.ApplicationContextHolder;
import com.github.yizzuide.milkomeda.util.JSONUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * RedisDeadQueue
 *
 * @author yizzuide
 * @since 3.0.8
 * @version 3.12.0
 * Create at 2020/04/17 00:51
 */
public class RedisDeadQueue implements DeadQueue, InitializingBean {

    private StringRedisTemplate redisTemplate;

    private String deadQueueKey = "ice:dead_queue";

    public RedisDeadQueue(IceProperties props) {
        if (!IceProperties.DEFAULT_INSTANCE_NAME.equals(props.getInstanceName())) {
            this.deadQueueKey = "ice:dead_queue:" + props.getInstanceName();
        }
    }

    @Override
    public void add(RedisOperations<String, String> operations, DelayJob delayJob) {
        operations.boundSetOps(this.deadQueueKey).add(JSONUtil.serialize(delayJob));
    }

    @Override
    public DelayJob pop() {
        String delayJob = getDeadQueue(this.deadQueueKey).pop();
        if (delayJob == null) {
            return null;
        }
        return JSONUtil.parse(delayJob, DelayJob.class);
    }

    @Override
    public List<DelayJob> pop(long count) {
        BoundSetOperations<String, String> deadQueue = getDeadQueue(this.deadQueueKey);
        Set<String> members = deadQueue.distinctRandomMembers(count);
        if (CollectionUtils.isEmpty(members)) {
            return null;
        }
        deadQueue.remove(members.toArray());
        return members.stream().map(s -> JSONUtil.parse(s, DelayJob.class)).collect(Collectors.toList());
    }

    @Override
    public List<DelayJob> popALL() {
        Set<String> members = getDeadQueue(this.deadQueueKey).members();
        if (CollectionUtils.isEmpty(members)) {
            return null;
        }
        return members.stream().map(s -> JSONUtil.parse(s, DelayJob.class)).collect(Collectors.toList());
    }

    private BoundSetOperations<String, String> getDeadQueue(String key) {
        return redisTemplate.boundSetOps(key);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        redisTemplate = ApplicationContextHolder.get().getBean(StringRedisTemplate.class);
    }

    @EventListener
    public void onApplicationEvent(IceInstanceChangeEvent event) {
        String instanceName = event.getSource().toString();
        this.deadQueueKey = "ice:dead_queue:" + instanceName;
    }
}
