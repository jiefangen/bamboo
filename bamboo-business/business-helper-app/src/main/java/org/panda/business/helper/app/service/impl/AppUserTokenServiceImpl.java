package org.panda.business.helper.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.collections4.CollectionUtils;
import org.panda.bamboo.common.constant.basic.Times;
import org.panda.business.helper.app.model.entity.AppUserToken;
import org.panda.business.helper.app.repository.AppUserTokenMapper;
import org.panda.business.helper.app.service.AppUserTokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * APP用户token 服务实现类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2024-06-11
 */
@Service
public class AppUserTokenServiceImpl extends ServiceImpl<AppUserTokenMapper, AppUserToken> implements AppUserTokenService {

    @Override
    public void refreshTokenStatus() {
        LambdaQueryWrapper<AppUserToken> queryWrapper = Wrappers.lambdaQuery();
        // 超过离线时间间隔未操作的token刷新至离线状态
        LocalDateTime currentDateTime = LocalDateTime.now();
        long offlineInterval = 3 * Times.S_ONE_MINUTE; // 3分钟/单位秒
        LocalDateTime offlineTime = currentDateTime.minusSeconds(offlineInterval);
        queryWrapper.eq(AppUserToken::getStatus, 1);
        queryWrapper.lt(AppUserToken::getUpdateTime, offlineTime);
        List<AppUserToken> userTokens = this.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(userTokens)) {
            List<AppUserToken> offlineUserTokens = userTokens.stream()
                    .map(userToken -> {
                        userToken.setStatus(2);
                        return userToken;
                    })
                    .collect(Collectors.toList());
            this.updateBatchById(offlineUserTokens);
        }

        LambdaQueryWrapper<AppUserToken> tokenQueryWrapper = Wrappers.lambdaQuery();
        tokenQueryWrapper.in(AppUserToken::getStatus, 1, 2);
        tokenQueryWrapper.lt(AppUserToken::getExpirationTime, currentDateTime);
        List<AppUserToken> appUserTokens = this.list(tokenQueryWrapper);
        // 刷新token令牌失效状态
        if (CollectionUtils.isNotEmpty(appUserTokens)) {
            List<AppUserToken> expirationUserTokens = appUserTokens.stream()
                    .map(userToken -> {
                        userToken.setStatus(3);
                        return userToken;
                    })
                    .collect(Collectors.toList());
            this.updateBatchById(expirationUserTokens);
        }
    }

    @Override
    public void cleanObsoleteToken() {
        long obsoleteInterval = 15 * Times.S_ONE_DAY; // 15天/单位秒
        LocalDateTime obsoleteTime = LocalDateTime.now().minusSeconds(obsoleteInterval);
        LambdaQueryWrapper<AppUserToken> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(AppUserToken::getStatus, 3, 4);
        queryWrapper.lt(AppUserToken::getCreateTime, obsoleteTime);
        List<AppUserToken> obsoleteTokens = this.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(obsoleteTokens)) {
            List<Long> idList = obsoleteTokens.stream()
                    .map(AppUserToken::getId)
                    .collect(Collectors.toList());
            this.removeByIds(idList);
        }
    }
}
