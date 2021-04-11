package com.github.yizzuide.milkomeda.atom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AtomLock
 *
 * @author yizzuide
 * @since 3.3.0
 * @version 3.3.1
 * Create at 2020/04/30 16:26
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AtomLock {
    /**
     * 分布式key，支持Spring EL
     * @return String
     */
    String key() default "";

    /**
     * 等待获取锁时间ms
     * @return -1等待直到获取锁
     */
    long waitTime() default -1;

    /**
     * 自动释放锁时间ms（ZK不需要这个特性）
     * @return -1不自动释放锁
     */
    long leaseTime() default 60000;

    /**
     * 加锁类型（ZK仅支持公平锁、读写锁）
     * @return AtomLockType
     */
    AtomLockType type() default AtomLockType.FAIR;

    /**
     * 是否只读（仅支持读写锁类型 {@link AtomLockType#READ_WRITE}）
     * @return true只读
     */
    boolean readOnly() default false;

    /**
     * 锁等待超时处理方案
     * @return AtomLockWaitTimeoutType
     * @since 3.3.1
     */
    AtomLockWaitTimeoutType waitTimeoutType() default AtomLockWaitTimeoutType.THROW_EXCEPTION;

    /**
     * 锁等待超时反馈处理
     * @return Spring EL表达式
     * @since 3.3.1
     */
    String fallback() default "";
}
