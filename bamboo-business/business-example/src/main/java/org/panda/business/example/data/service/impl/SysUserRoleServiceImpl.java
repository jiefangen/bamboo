package org.panda.business.example.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.panda.business.example.data.entity.SysRole;
import org.panda.business.example.data.entity.SysUser;
import org.panda.business.example.data.entity.SysUserRole;
import org.panda.business.example.data.repository.SysUserRoleMapper;
import org.panda.business.example.data.service.SysUserRoleService;
import org.panda.business.example.modules.components.mongo.SysUserMongoService;
import org.panda.business.example.modules.components.redis.SysUserCacheRepo;
import org.panda.business.example.modules.system.model.dto.SysUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户角色关系 服务实现类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-07
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Resource
    private SysUserCacheRepo sysUserCacheRepo;
    @Autowired
    private SysUserMongoService sysUserMongoService;

    @Override
//    @DataSourceSwitch(Datasource.DATASOURCE_ADMIN)
    public SysUserDto getUserAndRoles(String username) {
        if (sysUserCacheRepo.exists(username)) {
            return sysUserCacheRepo.find(username);
        }
        SysUser userParam = new SysUser();
        userParam.setUsername(username);
        SysUserDto sysUserDto = this.baseMapper.findUserAndRoles(userParam);
        if (sysUserDto != null) {
            List<SysRole> roles = sysUserDto.getRoles();
            if(CollectionUtils.isNotEmpty(roles)) {
                Set<String> roleCodes = roles.stream().map(SysRole::getRoleCode).collect(Collectors.toSet());
                sysUserDto.setRoleCodes(roleCodes);
            }

            // 用户数据单体缓存
            sysUserCacheRepo.save(sysUserDto);
            // 文档数据库存储
            sysUserMongoService.save(sysUserDto);
            return sysUserDto;
        }
        return null;
    }
}
