package com.github.yizzuide.milkomeda.comet.collector;

import com.github.yizzuide.milkomeda.comet.core.CometData;
import com.github.yizzuide.milkomeda.comet.core.CometRecorder;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * CollectorRecorder
 * 日志收集器记录器
 *
 * @author yizzuide
 * @since 1.15.0
 * @version 3.0.0
 * Create at 2019/11/13 19:18
 */
@Slf4j
public class CollectorRecorder implements CometRecorder {
    /**
     * 收集器工厂
     */
    private CollectorFactory collectorFactory;

    public CollectorRecorder(CollectorFactory collectorFactory) {
        this.collectorFactory = collectorFactory;
    }

    @Override
    public void onRequest(CometData prototype, String tag, HttpServletRequest request, Object[] args) {
        try {
            collectorFactory.get(tag).prepare(prototype);
        } catch (IllegalArgumentException e) {
            if (!e.getMessage().startsWith("type")) throw e;
        }
    }

    @Override
    public Object onReturn(CometData cometData, Object returnData) {
        try {
            collectorFactory.get(cometData.getTag()).onSuccess(cometData);
        } catch (IllegalArgumentException e) {
            if (!e.getMessage().startsWith("type")) throw e;
        }
        return returnData;
    }

    @Override
    public void onThrowing(CometData cometData, Exception e) {
        try {
            collectorFactory.get(cometData.getTag()).onFailure(cometData);
        } catch (IllegalArgumentException ex) {
            if (!ex.getMessage().startsWith("type")) throw ex;
        }
    }
}
