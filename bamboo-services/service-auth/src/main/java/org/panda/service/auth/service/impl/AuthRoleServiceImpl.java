package org.panda.service.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.panda.service.auth.model.entity.AuthRole;
import org.panda.service.auth.repository.AuthRoleMapper;
import org.panda.service.auth.service.AuthRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 应用认证角色 服务实现类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-10-25
 */
@Service
public class AuthRoleServiceImpl extends ServiceImpl<AuthRoleMapper, AuthRole> implements AuthRoleService {

}
