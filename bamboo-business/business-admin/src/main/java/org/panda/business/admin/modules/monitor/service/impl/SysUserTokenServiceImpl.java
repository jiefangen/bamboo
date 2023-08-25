package org.panda.business.admin.modules.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.business.admin.modules.monitor.api.param.OnlineQueryParam;
import org.panda.business.admin.modules.monitor.api.vo.OnlineVO;
import org.panda.business.admin.modules.monitor.service.SysActionLogService;
import org.panda.business.admin.modules.monitor.service.SysUserTokenService;
import org.panda.business.admin.modules.monitor.service.entity.SysActionLog;
import org.panda.business.admin.modules.monitor.service.entity.SysUserToken;
import org.panda.business.admin.modules.monitor.service.repository.SysUserTokenMapper;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.data.mybatis.util.QueryPageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户token 服务实现类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-07-21
 */
@Service
@Transactional
public class SysUserTokenServiceImpl extends ServiceImpl<SysUserTokenMapper, SysUserToken> implements SysUserTokenService {

    @Autowired
    private SysActionLogService actionLogService;

    @Override
    public QueryResult<OnlineVO> getOnlineByPage(OnlineQueryParam queryParam) {
        Page<SysUserToken> page = new Page<>(queryParam.getPageNo(), queryParam.getPageSize());
        LambdaQueryWrapper<SysUserToken> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserToken::getStatus, 1);
        if (StringUtils.isNotBlank(queryParam.getKeyword())) {
            queryWrapper.like(SysUserToken::getIdentity, queryParam.getKeyword());
        }
        queryWrapper.orderByDesc(SysUserToken::getCreateTime);
        IPage<SysUserToken> userTokenPage = this.page(page, queryWrapper);
        List<SysUserToken> tokenList = userTokenPage.getRecords();
        List<OnlineVO> onlineVOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tokenList)) {
            for (int i = 0; i < tokenList.size(); i++) {
                SysUserToken userToken = tokenList.get(i);
                LambdaQueryWrapper<SysActionLog> logWrapper = new LambdaQueryWrapper<>();
                logWrapper.eq(SysActionLog::getIdentity, userToken.getIdentity());
                logWrapper.eq(SysActionLog::getSourceId, userToken.getId());
                SysActionLog actionLog = actionLogService.getOne(logWrapper, false);

                OnlineVO onlineVO = new OnlineVO();
                onlineVO.transform(userToken, actionLog);
                onlineVOList.add(onlineVO);
            }
        }
        QueryResult<OnlineVO> queryResult = QueryPageHelper.convertQueryResult(onlineVOList, userTokenPage);
        return queryResult;
    }

    @Override
    public boolean quitOnlineUser(Long tokenId, String token) {
        SysUserToken userToken = this.getById(tokenId);
        if (token.equals(userToken.getToken())) {
            return false;
        }
        userToken.setStatus(4);
        return this.updateById(userToken);
    }

    @Override
    public void kickOutOnlineUsers(Integer userId, String username) {
        LambdaQueryWrapper<SysUserToken> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserToken::getUserId, userId);
        queryWrapper.eq(SysUserToken::getIdentity, username);
        queryWrapper.in(SysUserToken::getStatus, 1, 2);
        List<SysUserToken> userTokens = this.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(userTokens)) {
            List<SysUserToken> kickOutUserTokens = userTokens.stream()
                    .map(userToken -> {
                        userToken.setStatus(4);
                        return userToken;
                    })
                    .collect(Collectors.toList());
            this.updateBatchById(kickOutUserTokens);
        }
    }

    @Override
    public void refreshTokenStatus() {
        LambdaQueryWrapper<SysUserToken> queryWrapper = new LambdaQueryWrapper<>();
        // 超过离线时间间隔未操作的token刷新至离线状态
        LocalDateTime currentDateTime = LocalDateTime.now();
        long offlineInterval = 3 * 60L; // 3分钟
        LocalDateTime offlineTime = currentDateTime.minusSeconds(offlineInterval);
        queryWrapper.eq(SysUserToken::getStatus, 1);
        queryWrapper.lt(SysUserToken::getUpdateTime, offlineTime);
        List<SysUserToken> userTokens = this.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(userTokens)) {
            List<SysUserToken> offlineUserTokens = userTokens.stream()
                    .map(userToken -> {
                        userToken.setStatus(2);
                        return userToken;
                    })
                    .collect(Collectors.toList());
            this.updateBatchById(offlineUserTokens);
        }

        LambdaQueryWrapper<SysUserToken> tokenQueryWrapper = new LambdaQueryWrapper<>();
        tokenQueryWrapper.in(SysUserToken::getStatus, 1, 2);
        tokenQueryWrapper.lt(SysUserToken::getExpirationTime, currentDateTime);
        List<SysUserToken> sysUserTokens = this.list(tokenQueryWrapper);
        // 刷新token令牌失效状态
        if (CollectionUtils.isNotEmpty(sysUserTokens)) {
            List<SysUserToken> expirationUserTokens = sysUserTokens.stream()
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
        long obsoleteInterval = 7 * 24 * 60 * 60L; // 7天/单位秒
        LocalDateTime obsoleteTime = LocalDateTime.now().minusSeconds(obsoleteInterval);
        LambdaQueryWrapper<SysUserToken> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysUserToken::getStatus, 3, 4);
        queryWrapper.lt(SysUserToken::getCreateTime, obsoleteTime);
        List<SysUserToken> obsoleteTokens = this.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(obsoleteTokens)) {
            List<Long> idList = obsoleteTokens.stream()
                    .map(userToken -> userToken.getId())
                    .collect(Collectors.toList());
            this.removeByIds(idList);
        }
    }
}
