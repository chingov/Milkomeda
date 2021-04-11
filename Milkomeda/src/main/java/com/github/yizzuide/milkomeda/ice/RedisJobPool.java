package com.github.yizzuide.milkomeda.ice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.yizzuide.milkomeda.universe.context.ApplicationContextHolder;
import com.github.yizzuide.milkomeda.util.JSONUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RedisJobPool
 *
 * @author yizzuide
 * @since 1.15.0
 * @version 3.12.0
 * Create at 2019/11/16 15:45
 */
public class RedisJobPool implements JobPool, InitializingBean, ApplicationListener<IceInstanceChangeEvent> {

    private StringRedisTemplate redisTemplate;

    private String jobPoolKey = "ice:job_pool";

    public RedisJobPool(IceProperties props) {
        if (!IceProperties.DEFAULT_INSTANCE_NAME.equals(props.getInstanceName())) {
            this.jobPoolKey = "ice:job_pool" + ":" + props.getInstanceName();
        }
    }

    private BoundHashOperations<String, String, String> getPool () {
        return redisTemplate.boundHashOps(jobPoolKey);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void push(RedisOperations<String, String> operations, Job job) {
        operations.boundHashOps(jobPoolKey).put(job.getId(), JSONUtil.serialize(job));
    }

    @Override
    public <T> void push(RedisOperations<String, String> operations, List<Job<T>> jobs) {
        operations.boundHashOps(jobPoolKey).putAll(jobs.stream().collect(Collectors.toMap(Job::getId, JSONUtil::serialize)));
    }

    @Override
    public boolean exists(String jobId) {
        String job = getPool().get(jobId);
        return !StringUtils.isEmpty(job);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Job get(String jobId) {
        String job = getPool().get(jobId);
        if (null == job) return null;
        return JSONUtil.parse(job, Job.class);
    }

    @Override
    public <T> Job<T> getByType(String jobId, TypeReference<Job<T>> typeReference) {
        String job = getPool().get(jobId);
        if (null == job) return null;
        return JSONUtil.nativeRead(job, typeReference);
    }

    @Override
    public <T> List<Job<T>> getByType(List<String> jobIds, TypeReference<Job<T>> typeReference, int count) {
        List<String> jobOrigList = getPool().multiGet(jobIds);
        if (CollectionUtils.isEmpty(jobOrigList)) {
            return null;
        }

        List<Job<T>> jobList = new ArrayList<>();
        for (String job : jobOrigList) {
            if (null == job) {
                continue;
            }
            jobList.add(JSONUtil.nativeRead(job, typeReference));
        }
        return jobList;
    }

    @Override
    public List<Job<String>> getByStringType(List<String> jobIds, TypeReference<Job<String>> typeReference, int count) {
        return getByType(jobIds, typeReference, count);
    }

    @Override
    public void remove(Object... jobIds) {
        getPool().delete(jobIds);
    }

    @Override
    public void remove(RedisOperations<String, String> operations, Object... jobIds) {
        operations.boundHashOps(jobPoolKey).delete(jobIds);
    }

    @Override
    public void afterPropertiesSet() {
        redisTemplate = ApplicationContextHolder.get().getBean(StringRedisTemplate.class);
    }

    @Override
    public void onApplicationEvent(IceInstanceChangeEvent event) {
        String instanceName = event.getSource().toString();
        jobPoolKey = "ice:job_pool" + ":" + instanceName;
    }
}
