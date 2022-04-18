package org.panda.modules.system.controller;

import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.panda.common.constant.SystemConstants;
import org.panda.common.domain.ResultConstant;
import org.panda.common.domain.ResultVO;
import org.panda.common.exception.SystemException;
import org.panda.modules.system.domain.po.MenuPO;
import org.panda.modules.system.domain.po.RolePO;
import org.panda.modules.system.domain.vo.MenuVO;
import org.panda.modules.system.service.MenuService;
import org.panda.modules.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * 角色管理
 *
 * @author fangen
 * @since JDK 11 2022/4/16
 */
@Api(tags = "系统角色管理")
@RestController
@RequestMapping("/auth/system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @GetMapping("/getRoutes")
    public ResultVO getRoutes(){
        List<MenuVO> routes = menuService.getRoutes();
        return ResultVO.getSuccess(routes);
    }

    @GetMapping("/getRoles")
    public ResultVO getRoles(){
        List<RolePO> roles = roleService.getRoles();
        return ResultVO.getSuccess(roles);
    }

    @PostMapping("/add")
    public ResultVO add(@RequestBody RolePO role){
        String roleName = role.getRoleName();
        if(StringUtils.isEmpty(roleName)) {
            return ResultVO.getFailure(SystemConstants.PARAMETERS_INCOMPLETE);
        }
        String result = roleService.addRole(role);
        if (!ResultConstant.DEFAULT_SUCCESS_MSG.equals(result)) {
            return ResultVO.getFailure(result);
        }
        return ResultVO.getSuccess();
    }

    @DeleteMapping("/del/{roleId}")
    public ResultVO del(@PathVariable BigInteger roleId){
        try {
            int result = roleService.deleteRole(roleId);
            if (result < 1) {
                return ResultVO.getFailure(ResultConstant.DEFAULT_FAILURE_MSG);
            }
        }catch (SystemException e){
            return ResultVO.getFailure(e.getMessage());
        }
        return ResultVO.getSuccess();
    }
}
