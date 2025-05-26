package org.panda.support.security.config.interceptor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.support.security.executor.strategy.client.AuthServerClient;
import org.panda.support.security.model.ServiceNodeVO;
import org.panda.tech.core.config.CommonProperties;
import org.panda.tech.core.rpc.client.RpcClientReq;
import org.panda.tech.core.rpc.filter.RpcInvokeInterceptor;
import org.panda.tech.core.rpc.lb.LoadBalancer;
import org.panda.tech.core.rpc.lb.RoundRobinLoadBalancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * RPC调用多节点服务资源路由
 *
 * @author fangen
 **/
@Component
public class RpcActionInterceptor implements RpcInvokeInterceptor {
    // 负载均衡节点路由器
    private final LoadBalancer<String> lb = new RoundRobinLoadBalancer<>();

    @Autowired
    private CommonProperties commonProperties;
    @Autowired(required = false)
    private AuthServerClient authServerClient;

    @Override
    public void beforeInvoke(Object proxy, String beanId, Method method, Object[] args) throws Exception {
        if (authServerClient != null && proxy instanceof RpcClientReq) {
            Map<String, String> serviceRoots = commonProperties.getServiceRoots();
            String serverUrlRoot = serviceRoots.get(beanId);
            List<String> serverUrlRoots = null;
            if (StringUtils.isEmpty(serverUrlRoot)) { // 多节点动态服务路由
                Map<String, String> appServiceNames = commonProperties.getAppServiceNames();
                String appServiceName = appServiceNames.get(beanId);
                if (StringUtils.isNotEmpty(appServiceName)) {
                    ServiceNodeVO serviceNodeVO = authServerClient.getServiceNodes(appServiceName);
                    serverUrlRoots = serviceNodeVO.getDirectUris();
                }
            } else {
                if (serverUrlRoot.contains(Strings.COMMA)) { // 多节点路由
                    serverUrlRoots = Arrays.asList(serverUrlRoot.split(Strings.COMMA));
                }
            }
            // 服务节点路由算法
            if (CollectionUtils.isNotEmpty(serverUrlRoots)) {
                RpcClientReq targetProxy = (RpcClientReq) proxy;
                targetProxy.setServerUrlRoot(lb.select(serverUrlRoots));
            }
        }
        LogUtil.info(getClass(), "beanId: {}, methodName: {}, args: {}", beanId, method.getName(), args);
    }

    @Override
    public void afterInvoke(String beanId, Method method, Object[] args, Object result) {
        LogUtil.info(getClass(), "beanId: {}, methodName: {}, result: {}", beanId, method.getName(), JsonUtil.toJson(result));
    }
}
