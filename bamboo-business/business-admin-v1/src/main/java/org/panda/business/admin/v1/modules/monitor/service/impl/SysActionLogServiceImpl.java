package org.panda.business.admin.v1.modules.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.panda.business.admin.v1.modules.monitor.api.param.LogQueryParam;
import org.panda.business.admin.v1.modules.monitor.service.SysActionLogService;
import org.panda.business.admin.v1.modules.monitor.service.entity.SysActionLog;
import org.panda.business.admin.v1.modules.monitor.service.repository.SysActionLogMapper;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.data.mybatis.config.QueryPageHelper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统操作日志 服务实现类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-30
 */
@Service
public class SysActionLogServiceImpl extends ServiceImpl<SysActionLogMapper, SysActionLog> implements SysActionLogService {

    @Override
    public QueryResult<SysActionLog> getLogByPage(LogQueryParam queryParam) {
        Page<SysActionLog> page = new Page<>(queryParam.getPageNo(), queryParam.getPageSize());
        LambdaQueryWrapper<SysActionLog> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(queryParam.getKeyword())) {
            queryWrapper.like(SysActionLog::getIdentity, queryParam.getKeyword())
                    .like(SysActionLog::getRemoteAddress, queryParam.getKeyword());
        }
        queryWrapper.orderByDesc(SysActionLog::getOperatingTime);
        IPage<SysActionLog> actionLogPage = this.page(page, queryWrapper);
        QueryResult<SysActionLog> queryResult = QueryPageHelper.convertQueryResult(actionLogPage);
        return queryResult;
    }

    @Override
    public void deleteAllLog() {
        this.baseMapper.truncateLog();
    }

    @Override
    public int removeLogByTime(int intervalDay) {
        return this.baseMapper.deleteLogByTime(intervalDay);
    }
}
