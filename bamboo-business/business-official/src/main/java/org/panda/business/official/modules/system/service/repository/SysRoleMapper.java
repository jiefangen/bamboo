package org.panda.business.official.modules.system.service.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.panda.business.official.modules.system.service.entity.SysRole;

import java.util.List;

/**
 * <p>
 * 系统角色 Mapper 接口
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-06
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> findByUserId(@Param("userId") Integer userId);

}
