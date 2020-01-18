package com.github.yizzuide.milkomeda.ice;

import com.github.yizzuide.milkomeda.util.Polyfill;
import com.github.yizzuide.milkomeda.util.RedisUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * DelayJobHandler
 *
 * @author yizzuide
 * @since 1.15.0
 * @version 2.1.1
 * Create at 2019/11/16 17:30
 */
@Slf4j
@Data
@AllArgsConstructor
public class DelayJobHandler implements Runnable {

    private StringRedisTemplate redisTemplate;

    /**
     * 任务池
     */
    private JobPool jobPool;

    /**
     * 延迟队列
     */
    private DelayBucket delayBucket;

    /**
     * 待处理队列
     */
    private ReadyQueue readyQueue;

    /**
     * 索引
     */
    private int index;

    // 延迟桶分布式锁Key
    private static final String KEY_IDEMPOTENT_LIMITER = "ice:execute_delay_bucket_lock";

    @Autowired
    private IceProperties props;

    @Override
    public void run() {
        // 延迟桶处理锁住资源，防止多线程并发执行时出现相同记录问题
        boolean absent = RedisUtil.setIfAbsent(KEY_IDEMPOTENT_LIMITER, props.getTaskPopCountLockTimeoutSeconds(), redisTemplate);
        if (absent) return;

        DelayJob delayJob = null;
        try {
            delayJob = delayBucket.poll(index);
            // 没有任务
            if (delayJob == null) {
                return;
            }

            // 发现延时任务，延迟时间没到
            long currentTime = System.currentTimeMillis();
            if (delayJob.getDelayTime() > currentTime) {
                return;
            }

            // 获取超时任务（包括延迟超时和TTR超时）
            Job<?> job = jobPool.get(delayJob.getJodId());
            // 任务元数据不存在（说明任务已被消费）
            if (job == null) {
                // 移除TTR超时检测任务
                delayBucket.remove(index, delayJob);
                return;
            }
            JobStatus status = job.getStatus();
            if (JobStatus.RESERVED.equals(status)) {
                // 处理超时任务
                processTtrJob(delayJob, job);
            } else {
                // 延时任务
                processDelayJob(delayJob, job);
            }
        } catch (Exception e) {
            log.error("Ice Timer处理延迟Job {} 异常：{}", delayJob != null ?
                    delayJob.getJodId()  : "[任务数据获取失败]", e.getMessage(), e);
        } finally {
            // 删除Lock
            Polyfill.redisDelete(redisTemplate, KEY_IDEMPOTENT_LIMITER);
        }
    }

    /**
     * 处理ttr的任务
     */
    private void processTtrJob(DelayJob delayJob, Job<?> job) {
        log.info("Ice处理TTR重试的Job {}，已重试次数为{}", delayJob.getJodId(), delayJob.getRetryCount());
        // 检测重试次数过载
        boolean overload = delayJob.getRetryCount() > job.getRetryCount();
        if (overload) {
            log.error("Ice检测到 Job {} 的TTR超时重试超过预设的{}次，当前重试次数为{}", job.getId(),
                    job.getRetryCount(), delayJob.getRetryCount());
        }

        RedisUtil.batchOps(() -> {
            // 还原到延迟状态
            job.setStatus(JobStatus.DELAY);
            jobPool.push(job);
            // 移除delayBucket中的任务
            delayBucket.remove(index, delayJob);
            // 设置当前重试次数
            if (delayJob.getRetryCount() < Integer.MAX_VALUE) {
                delayJob.setRetryCount(delayJob.getRetryCount() + 1);
            }
            // 重置到当前延迟
            long delayDate = System.currentTimeMillis() +
                    (props.isEnableDelayMultiRetryCount() ?
                            job.getDelay() * (delayJob.getRetryCount() + 1) * props.getRetryDelayMultiFactor() :
                            job.getDelay());
            delayJob.setDelayTime(delayDate);
            // 再次添加到任务中
            delayBucket.add(delayJob);
        }, redisTemplate);
    }

    /**
     * 处理延时任务
     */
    private void processDelayJob(DelayJob delayJob, Job<?> job) {
        log.info("Ice正在处理延迟的Job {}，当前状态为：{}", delayJob.getJodId(), job.getStatus());
        RedisUtil.batchOps(() -> {
            // 修改任务池状态
            job.setStatus(JobStatus.READY);
            jobPool.push(job);
            // 设置到待处理任务
            readyQueue.push(delayJob);
            // 移除delayBucket中的任务
            delayBucket.remove(index, delayJob);
        }, redisTemplate);
    }
}
