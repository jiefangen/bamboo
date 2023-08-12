package org.panda.tech.data.datasource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.panda.tech.data.annotation.DataSourceSwitch;
import org.panda.tech.data.common.DataCommons;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 数据源切换切面
 *
 * @author fangen
 **/
@Aspect
@Component
public class DataSourceSwitchAspect {

    @Pointcut("@annotation(org.panda.tech.data.annotation.DataSourceSwitch)")
    public void dataSourcePointCut() {
    }

    @Around("dataSourcePointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        DataSourceSwitch dataSourceSwitch = method.getAnnotation(DataSourceSwitch.class);
        if (dataSourceSwitch != null) {
            DynamicDataSourceContextHolder.setDataSourceKey(dataSourceSwitch.value());
        } else {
            DynamicDataSourceContextHolder.setDataSourceKey(DataCommons.DATASOURCE_PRIMARY);
        }
        try {
            return joinPoint.proceed();
        } finally {
            DynamicDataSourceContextHolder.clearDataSourceKey();
        }
    }
}

