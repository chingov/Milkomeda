package com.github.yizzuide.milkomeda.ice;

import com.github.yizzuide.milkomeda.universe.context.AopContextHolder;
import com.github.yizzuide.milkomeda.universe.metadata.HandlerMetaData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IceContext
 *
 * @author yizzuide
 * @since 1.15.0
 * @version 3.0.7
 * Create at 2019/11/17 18:40
 */
@Slf4j
public class IceContext implements ApplicationListener<ContextRefreshedEvent> {

    private static Map<String, List<HandlerMetaData>> topicMap = new HashMap<>();

    private static Map<String, List<HandlerMetaData>> topicTtrMap = new HashMap<>();

    private static Map<String, List<HandlerMetaData>> topicTtrOverloadMap = new HashMap<>();


    @Autowired
    private IceProperties props;

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        topicMap = AopContextHolder.getHandlerMetaData(IceHandler.class, IceListener.class, (annotation, handlerAnnotation, metaData) -> {
                    IceListener iceListener = (IceListener) annotation;
                    return iceListener.value();
                }, !props.isMultiTopicListenerPerHandler());
        topicTtrMap = AopContextHolder.getHandlerMetaData(IceHandler.class, IceTtrListener.class, (annotation, handlerAnnotation, metaData) -> {
            IceTtrListener iceTtrListener = (IceTtrListener) annotation;
            return iceTtrListener.value();
        }, !props.isMultiTopicListenerPerHandler());
        topicTtrOverloadMap = AopContextHolder.getHandlerMetaData(IceHandler.class, IceTtrOverloadListener.class, (annotation, handlerAnnotation, metaData) -> {
            IceTtrOverloadListener iceTtrOverloadListener = (IceTtrOverloadListener) annotation;
            return iceTtrOverloadListener.value();
        }, !props.isMultiTopicListenerPerHandler());
    }

    static Map<String, List<HandlerMetaData>> getTopicMap() {
        return topicMap;
    }

    static Map<String, List<HandlerMetaData>> getTopicTtrMap() {
        return topicTtrMap;
    }

    static Map<String, List<HandlerMetaData>> getTopicTtrOverloadMap() {
        return topicTtrOverloadMap;
    }
}
