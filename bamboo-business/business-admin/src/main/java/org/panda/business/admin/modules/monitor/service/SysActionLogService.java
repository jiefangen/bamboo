package org.panda.business.admin.modules.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.panda.business.admin.modules.monitor.api.param.LogQueryParam;
import org.panda.business.admin.modules.monitor.service.entity.SysActionLog;
import org.panda.tech.data.model.query.QueryResult;

/**
 * <p>
 * 系统操作日志 服务类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-30
 */
public interface SysActionLogService extends IService<SysActionLog> {

    QueryResult<SysActionLog> getLogByPage(LogQueryParam queryParam);

    void deleteAllLog();

    int removeLogByTime(int intervalDay);
}
