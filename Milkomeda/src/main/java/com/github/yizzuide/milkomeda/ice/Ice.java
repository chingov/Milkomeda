package com.github.yizzuide.milkomeda.ice;

import java.util.List;

/**
 * Ice
 * 最外层接口
 *
 * @author yizzuide
 * @since 1.15.0
 * Create at 2019/11/16 15:11
 */
public interface Ice {
    /**
     * 添加延迟任务
     * @param job   Job
     */
    void add(Job job);

    /**
     * 添加延迟任务
     * @param id    任务id
     * @param topic 任务分组
     * @param body  业务数据
     * @param delay 延迟时间ms
     * @param <T>   业务数据类型
     */
    <T> void add(String id, String topic, T body, long delay);

    /**
     * 取出待处理任务
     * @param topic 任务分组
     * @param <T>   业务数据
     * @return  Job
     */
    <T> Job<T> pop(String topic);

    /**
     * 批量取出待处理任务
     * @param topic 任务分组
     * @param count 批量数
     * @param <T>   业务数据
     * @return List
     */
    <T> List<Job<T>> pop(String topic, int count);

    /**
     * 完成任务
     * @param jobs    任务列表
     * @param <T>   业务数据
     */
    <T> void finish(List<Job<T>> jobs);

    /**
     * 完成任务
     * @param jobIds    任务id列表
     */
    void finish(Object... jobIds);

    /**
     * 删除任务
     * @param jobs    任务列表
     * @param <T>   业务数据
     */
    <T> void delete(List<Job<T>> jobs);

    /**
     * 删除任务
     * @param jobIds    任务id列表
     */
    void delete(Object... jobIds);
}
