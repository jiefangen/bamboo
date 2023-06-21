package org.panda.business.admin.v1.modules.system.service.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.panda.business.admin.v1.modules.system.service.entity.SysRole;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 系统角色 Mapper 接口
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-06
 */
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> findRolesByUserId(@Param("userId") Integer userId);

}
