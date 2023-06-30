package org.panda.business.admin.v1.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.panda.business.admin.v1.modules.system.service.SysActionLogService;
import org.panda.business.admin.v1.modules.system.service.entity.SysActionLog;
import org.panda.business.admin.v1.modules.system.service.repository.SysActionLogMapper;
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
    public int removeLogByTime(int intervalDay) {
        return this.baseMapper.deleteLogByTime(intervalDay);
    }
}
