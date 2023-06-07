package org.panda.business.official.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.panda.business.official.modules.system.service.SysUserService;
import org.panda.business.official.modules.system.service.dto.SysUserDto;
import org.panda.business.official.modules.system.service.entity.SysRole;
import org.panda.business.official.modules.system.service.entity.SysUser;
import org.panda.business.official.modules.system.service.repository.SysRoleMapper;
import org.panda.business.official.modules.system.service.repository.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public SysUser getUserInfo(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        return sysUser;
    }

    @Override
    public SysUserDto getUserAndRoles(String username) {
        SysUser sysUser = this.getUserInfo(username);
        if (sysUser == null) {
            return null;
        }
        SysUserDto userDto = new SysUserDto(sysUser);
        List<SysRole> roles = sysRoleMapper.findByUserId(userDto.getId());
        userDto.setRoles(roles);
        if(CollectionUtils.isNotEmpty(roles)) {
            Set<String> roleCodes = roles.stream().map(role -> role.getRoleCode()).collect(Collectors.toSet());
            userDto.setRoleCodes(roleCodes);
        }
        return userDto;
    }


}
