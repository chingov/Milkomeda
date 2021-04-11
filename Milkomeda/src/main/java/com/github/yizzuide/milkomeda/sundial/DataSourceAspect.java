package com.github.yizzuide.milkomeda.sundial;

import com.github.yizzuide.milkomeda.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

/**
 * Sundial注解切面
 * @author jsq 786063250@qq.com
 * @since 3.4.0
 * @version 3.8.0
 * Create at 2020/5/8
 */
@Slf4j
@Order(999)
@Aspect
public class DataSourceAspect {

    @Pointcut("@within(com.github.yizzuide.milkomeda.sundial.Sundial) && execution(public * *(..))")
    public void classPointCut() {
    }


    @Pointcut("@annotation(com.github.yizzuide.milkomeda.sundial.Sundial) && execution(public * *(..))")
    public void actionPointCut() {
    }

    @Around("actionPointCut() || classPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Sundial sundial = ReflectUtil.getAnnotation(joinPoint, Sundial.class);
        if (StringUtils.isEmpty(sundial.value())) {
            return joinPoint.proceed();
        }
        SundialHolder.setDataSourceType(sundial.value());
        try {
            return joinPoint.proceed();
        } finally {
            SundialHolder.clearDataSourceType();
        }
    }
}
