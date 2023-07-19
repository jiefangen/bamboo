package org.panda.business.admin.modules.monitor.service.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.panda.business.admin.modules.monitor.service.entity.SysActionLog;

/**
 * <p>
 * 系统操作日志 Mapper 接口
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-30
 */
public interface SysActionLogMapper extends BaseMapper<SysActionLog> {

    void truncateLog();

    int deleteLogByTime(@Param("intervalDay") int intervalDay);
}
