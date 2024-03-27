package org.panda.tech.core.config.app.security;

import org.panda.bamboo.core.beans.ContextInitializedBean;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 抽象认证授权支持
 *
 * @author fangen
 **/
public abstract class AbstractAuthSupport implements ContextInitializedBean {

    private final Map<Class<?>, AuthManagerStrategy> authStrategyContainer = new HashMap<>();

    @Override
    public void afterInitialized(ApplicationContext context) throws Exception {
        context.getBeansOfType(AuthManagerStrategy.class).forEach((id, authStrategy) -> {
            Class<?> strategyType = authStrategy.getClass();
            if (!authStrategyContainer.containsKey(strategyType)) {
                this.authStrategyContainer.put(strategyType, authStrategy);
            }
        });
    }

    protected AuthManagerStrategy getAuthStrategy() {
        return authStrategyContainer.get(getStrategyType());
    }

    protected boolean isNotAuthStrategy() {
        return this.getAuthStrategy() == null;
    }

    protected abstract Class<?> getStrategyType();

}
