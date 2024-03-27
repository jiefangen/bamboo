package org.panda.tech.core.jwt.internal;

import org.panda.bamboo.common.constant.Profiles;
import org.panda.bamboo.core.context.SpringContextHolder;

/**
 * 抽象的内部JWT配置。
 */
public abstract class AbstractInternalJwtConfiguration implements InternalJwtConfiguration {

    @Override
    public String getAppName() {
        return null;
    }

    @Override
    public int getExpiredIntervalSeconds() {
        return getExpiredIntervalSeconds(SpringContextHolder.getActiveProfile());
    }

    /**
     * 获取JWT过期时间秒数
     *
     * @param profile 运行环境
     * @return JWT的过期时间秒数
     */
    protected int getExpiredIntervalSeconds(String profile) {
        switch (profile) {
            case Profiles.LOCAL:
            case Profiles.DEV:
                return 3600;
            case Profiles.TEST:
                return 1800;
            default:
                return 60;
        }
    }

}
