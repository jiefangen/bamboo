package org.panda.business.admin.modules.monitor.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.panda.business.admin.modules.monitor.model.ActionLog;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionLogDao {

    Page<ActionLog> findLogPage(@Param("keyword") String keyword);

    void insertLog(@Param("log") ActionLog log);

    void truncateLog();

    int deleteLogByTime(@Param("intervalDay") int intervalDay);
}
