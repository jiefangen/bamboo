package org.panda.business.admin.modules.system.controller;

import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.panda.business.admin.common.constant.SystemConstants;
import org.panda.business.admin.common.constant.annotation.ControllerWebLog;
import org.panda.business.admin.common.constant.enums.ActionType;
import org.panda.business.admin.common.model.ResultConstant;
import org.panda.business.admin.common.model.ResultVO;
import org.panda.business.admin.common.exception.SystemException;
import org.panda.business.admin.modules.system.model.dto.RoleDTO;
import org.panda.business.admin.modules.system.model.po.RolePO;
import org.panda.business.admin.modules.system.model.vo.MenuVO;
import org.panda.business.admin.modules.system.service.MenuService;
import org.panda.business.admin.modules.system.service.RoleService;
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
    @ControllerWebLog(content = "/system/role/getRoles", actionType = ActionType.QUERY)
    public ResultVO getRoles(){
        List<RoleDTO> roles = roleService.getRoles();
        return ResultVO.getSuccess(roles);
    }

    @PostMapping("/add")
    @ControllerWebLog(content = "/system/role/add", actionType = ActionType.ADD, intoDb = true)
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

    @PutMapping("/update/{roleId}")
    @ControllerWebLog(content = "/system/role/update", actionType = ActionType.AUTH, intoDb = true)
    public ResultVO update(@PathVariable BigInteger roleId, @RequestBody RoleDTO roleDTO){
        roleService.updateRole(roleId, roleDTO);
        return ResultVO.getSuccess();
    }

    @DeleteMapping("/del/{roleId}")
    @ControllerWebLog(content = "/system/role/del", actionType = ActionType.DEL, intoDb = true)
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
