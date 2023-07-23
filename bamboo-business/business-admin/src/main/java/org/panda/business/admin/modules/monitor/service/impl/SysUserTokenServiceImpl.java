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
import org.panda.tech.data.mybatis.config.QueryPageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统用户token 服务实现类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-07-21
 */
@Service
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
}
