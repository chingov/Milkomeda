package com.github.yizzuide.milkomeda.pulsar;

import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * PulsarHolder
 * Pulsar 静态资源引用类
 *
 * @author yizzuide
 * @since 1.0.0
 * @version 1.5.0
 * Create at 2019/04/30 15:47
 */
public class PulsarHolder {

    private static Function<Throwable, Object> errorCallback;

    private static Pulsar pulsar;

    static void setErrorCallback(Function<Throwable, Object> errorCallback) {
        PulsarHolder.errorCallback = errorCallback;
    }

    static void setPulsar(Pulsar pulsar) { PulsarHolder.pulsar = pulsar; }

    /**
     * 可抛出异常回调，外部可直接调用来触发设置的回调执行
     * @return 异常回调
     */
    public static Function<Throwable, Object> getErrorCallback() {
        return errorCallback;
    }

    /**
     * 获取Pulsar
     * @return Pulsar
     */
    public static Pulsar getPulsar() {
        return pulsar;
    }

    /**
     * 通过 Callable 和 PulsarDeferredResult 推迟运行耗时请求处理再返回
     *
     * @param callable  运行方法，业务代码里可以直接返回数据。如：return ResponseEntity.ok(data);
     * @param identifier    PulsarDeferredResult或PulsarDeferredResultID
     * @return 返回null用于配合 @PulsarFlow 的使用，其它地方使用可以忽略这个返回值（因为这个不是真实要返回的数据）
     */
    public static Object defer(Callable<Object> callable, Object identifier) {
        DeferredResult<Object> deferredResult;
        if (identifier instanceof String || identifier instanceof Integer || identifier instanceof Long) {
            deferredResult = pulsar.getDeferredResult(String.valueOf(identifier));
        } else if (identifier instanceof PulsarDeferredResult) {
            deferredResult = ((PulsarDeferredResult) identifier).getDeferredResult();
        } else {
            throw new IllegalArgumentException("identifier " + identifier + " is invalid.");
        }
        pulsar.post(new PulsarRunner(callable, deferredResult));
        return null;
    }
}
