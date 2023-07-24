package org.panda.business.admin.modules.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.panda.business.admin.common.model.WebLogData;
import org.panda.business.admin.modules.monitor.api.param.LogQueryParam;
import org.panda.business.admin.modules.monitor.service.entity.SysActionLog;
import org.panda.business.admin.modules.monitor.service.entity.SysUserToken;
import org.panda.tech.data.model.query.QueryResult;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 系统操作日志 服务类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-30
 */
public interface SysActionLogService extends IService<SysActionLog> {

    void intoLogDb(WebLogData webLogData, Object res);

    void intoLoginLog(HttpServletRequest request, SysUserToken userToken);

    QueryResult<SysActionLog> getLogByPage(LogQueryParam queryParam);

    void deleteAllLog();

    void cleanObsoleteLog();
}
