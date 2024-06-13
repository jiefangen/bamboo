package org.panda.business.helper.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.panda.business.helper.app.common.model.WebLogData;
import org.panda.business.helper.app.model.entity.AppActionLog;

/**
 * <p>
 * APP操作日志 服务类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2024-06-11
 */
public interface AppActionLogService extends IService<AppActionLog> {

    void intoLogDbAsync(WebLogData webLogData, Object res);
}
