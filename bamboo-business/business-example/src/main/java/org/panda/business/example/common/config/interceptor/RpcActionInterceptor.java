package org.panda.business.example.common.config.interceptor;

import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.tech.core.rpc.filter.RpcInvokeInterceptor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * RPC调用动作拦截
 *
 * @author fangen
 **/
@Component
public class RpcActionInterceptor implements RpcInvokeInterceptor {

    @Override
    public void beforeInvoke(String beanId, Method method, Object[] args) throws Exception {
        LogUtil.info(getClass(), "beanId: {}, args: {}", beanId, args);
    }

    @Override
    public void afterInvoke(String beanId, Method method, Object[] args, Object result) {
        LogUtil.info(getClass(), "beanId: {}, result: {}", beanId, JsonUtil.toJson(result));
    }

}
