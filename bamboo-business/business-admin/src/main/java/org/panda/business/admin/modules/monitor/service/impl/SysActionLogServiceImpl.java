package org.panda.business.admin.modules.monitor.service.impl;

import cn.hutool.http.useragent.UserAgent;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.annotation.helper.EnumValueHelper;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.date.TemporalUtil;
import org.panda.business.admin.application.resolver.MessageSourceResolver;
import org.panda.business.admin.common.model.WebLogData;
import org.panda.business.admin.common.utils.CommonUtils;
import org.panda.business.admin.modules.monitor.api.param.LogQueryParam;
import org.panda.business.admin.modules.monitor.service.SysActionLogService;
import org.panda.business.admin.modules.monitor.service.entity.SysActionLog;
import org.panda.business.admin.modules.monitor.service.entity.SysUserToken;
import org.panda.business.admin.modules.monitor.service.repository.SysActionLogMapper;
import org.panda.tech.core.exception.business.BusinessException;
import org.panda.tech.core.spec.log.ActionType;
import org.panda.tech.core.web.model.IPAddress;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.restful.ResultEnum;
import org.panda.tech.core.web.util.IP2RegionUtil;
import org.panda.tech.core.web.util.NetUtil;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.data.mybatis.util.QueryPageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统操作日志 服务实现类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-30
 */
@Service
@Transactional
public class SysActionLogServiceImpl extends ServiceImpl<SysActionLogMapper, SysActionLog> implements SysActionLogService {

    @Autowired
    private MessageSourceResolver messageSourceResolver;

    private final IP2RegionUtil ip2RegionUtil = new IP2RegionUtil();

    @Async
    @Override
    public void intoLogDb(WebLogData webLogData, Object res) {
        SysActionLog actionLog = new SysActionLog();
        String remoteAddress = webLogData.getHost();
        actionLog.setHost(remoteAddress);
        actionLog.setIpAddress(this.getIpAttribution(remoteAddress));
        actionLog.setTerminalDevice(webLogData.getTerminalDevice());
        actionLog.setTerminalOs(webLogData.getTerminalOs());
        actionLog.setIdentity(webLogData.getIdentity());
        actionLog.setSourceId(webLogData.getSourceId());
        long startTimeMillis = webLogData.getStartTimeMillis();
        LocalDateTime startDateTime = TemporalUtil.toLocalDateTime(Instant.ofEpochMilli(startTimeMillis));
        actionLog.setOperatingTime(startDateTime);
        actionLog.setElapsedTime(webLogData.getTakeTime());
        actionLog.setActionType(webLogData.getActionType());
        actionLog.setContent(webLogData.getContent());
        // 返回结果解析处理
        if (res instanceof RestfulResult) {
            RestfulResult<?> result = (RestfulResult<?>) res;
            if (result.getCode() != ResultEnum.SUCCESS.getCode()) {
                actionLog.setExceptionInfo(result.getMessage());
            }
            actionLog.setStatusCode(result.getCode());
        } else if (res instanceof Throwable) {
            Throwable throwable = (Throwable) res;
            actionLog.setExceptionInfo(throwable.getMessage());
            if (throwable instanceof BusinessException) {
                BusinessException businessException = (BusinessException) throwable;
                actionLog.setStatusCode(businessException.getCode());
            } else {
                actionLog.setStatusCode(ResultEnum.FAILURE.getCode());
            }
        } else {
            actionLog.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        this.save(actionLog);
    }

    @Override
    public void intoLoginLog(HttpServletRequest request, SysUserToken userToken) {
        SysActionLog actionLog = new SysActionLog();
        String actionTypeValue = EnumValueHelper.getValue(ActionType.LOGIN);
        actionLog.setActionType(actionTypeValue);
        actionLog.setContent("/login");
        if (userToken != null) {
            actionLog.setIdentity(userToken.getIdentity());
            actionLog.setSourceId(String.valueOf(userToken.getId()));
        }
        String userAgentHeader = request.getHeader(HttpHeaders.USER_AGENT);
        String remoteAddress = WebHttpUtil.getRemoteAddress(request);
        actionLog.setHost(remoteAddress);
        actionLog.setIpAddress(ip2RegionUtil.getIPRegionCache("198.19.249.3"));
        UserAgent userAgent = WebHttpUtil.getUserAgent(userAgentHeader);
        if (userAgent != null) {
            actionLog.setTerminalDevice(userAgent.getBrowser().getName() + Strings.SPACE + userAgent.getVersion());
            actionLog.setTerminalOs(userAgent.getOs().getName() + Strings.SPACE + userAgent.getOsVersion());
        }
        actionLog.setOperatingTime(LocalDateTime.now());
        actionLog.setElapsedTime(0L);
        actionLog.setStatusCode(Commons.RESULT_SUCCESS_CODE);
        this.save(actionLog);
    }

    private String getIpAttribution(String remoteAddress) {
        String ipAttribution;
        if (!NetUtil.isIntranetIp(remoteAddress)) {
            String locale = CommonUtils.getGlobalLanguage();
            IPAddress ipAddress = WebHttpUtil.getIPAddress(remoteAddress, locale);
            String regionName = ipAddress.getRegionName();
            String city = ipAddress.getCity();
            if (StringUtils.isBlank(regionName) && StringUtils.isBlank(city)) {
                ipAttribution = messageSourceResolver.findI18nMessage("admin.monitor.actionLog.ipInvalid");
            } else {
                ipAttribution = regionName + Strings.VERTICAL_BAR + city;
            }
        } else {
            ipAttribution = messageSourceResolver.findI18nMessage("admin.monitor.actionLog.ipAttribution");
        }
        return ipAttribution;
    }

    @Override
    public QueryResult<SysActionLog> getLogByPage(LogQueryParam queryParam) {
        Page<SysActionLog> page = new Page<>(queryParam.getPageNo(), queryParam.getPageSize());
        LambdaQueryWrapper<SysActionLog> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(queryParam.getKeyword())) {
            queryWrapper.like(SysActionLog::getIdentity, queryParam.getKeyword())
                    .like(SysActionLog::getHost, queryParam.getKeyword());
        }
        queryWrapper.orderByDesc(SysActionLog::getOperatingTime);
        IPage<SysActionLog> actionLogPage = this.page(page, queryWrapper);
        return QueryPageHelper.convertQueryResult(actionLogPage);
    }

    @Override
    public void deleteAllLog() {
        this.baseMapper.truncateLog();
    }

    @Override
    public void cleanObsoleteLog() {
        long obsoleteInterval = 30 * 24 * 60 * 60L; // 30天/单位秒
        LocalDateTime obsoleteTime = LocalDateTime.now().minusSeconds(obsoleteInterval);
        LambdaQueryWrapper<SysActionLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.lt(SysActionLog::getOperatingTime, obsoleteTime);
        List<SysActionLog> obsoleteLogs = this.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(obsoleteLogs)) {
            List<Long> idList = obsoleteLogs.stream()
                    .map(log -> log.getId())
                    .collect(Collectors.toList());
            this.removeByIds(idList);
        }
    }
}
