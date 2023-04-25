package org.panda.business.admin.modules.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.panda.business.admin.common.constant.SystemConstants;
import org.panda.business.admin.common.constant.annotation.ControllerWebLog;
import org.panda.business.admin.common.constant.enumeration.ActionType;
import org.panda.business.admin.common.model.ResultConstant;
import org.panda.business.admin.common.model.ResultVO;
import org.panda.business.admin.common.exception.SystemException;
import org.panda.business.admin.common.utils.EncryptUtil;
import org.panda.business.admin.common.utils.TokenUtil;
import org.panda.business.admin.modules.system.model.dto.UserDTO;
import org.panda.business.admin.modules.system.model.param.UserQueryParam;
import org.panda.business.admin.modules.system.model.po.UserPO;
import org.panda.business.admin.modules.system.model.vo.MenuVO;
import org.panda.business.admin.modules.system.service.MenuService;
import org.panda.business.admin.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理
 *
 * @author jiefangen
 * @since JDK 1.8  2020/5/5
 **/
@Api(tags = "系统用户管理")
@RestController
@RequestMapping("/auth/system/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @PostMapping("/page")
    @ControllerWebLog(content = "/system/user/page", actionType = ActionType.QUERY, intoDb = true)
//  @RequiresPermissions(value = "permis[get]")
    public ResultVO page(@RequestBody UserQueryParam param){
        param.initPage();
        Page<UserDTO> users = userService.getUsers(param.getKeyword());
        PageInfo<UserDTO> pageInfo = new PageInfo<>(users);
        return ResultVO.getSuccess(pageInfo);
    }

    @PostMapping("/add")
    @ControllerWebLog(content = "/system/user/add", actionType = ActionType.ADD, intoDb = true)
//    @RequiresRoles(value={"SYSTEM","ADMIN"},logical= Logical.OR)
    public ResultVO add(@RequestBody UserPO user){
        String username = user.getUsername();
        String password = user.getPassword();
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return ResultVO.getFailure(SystemConstants.PARAMETERS_INCOMPLETE);
        }

        EncryptUtil encryptUtil = new EncryptUtil();
        encryptUtil.encryptedPwd(user);
        String result = userService.addUser(user);
        if (!ResultConstant.DEFAULT_SUCCESS_MSG.equals(result)) {
            return ResultVO.getFailure(result);
        }

        return ResultVO.getSuccess();
    }

    @PutMapping("/edit")
    @ControllerWebLog(content = "/system/user/edit", actionType = ActionType.UPDATE, intoDb = true)
    public ResultVO edit(@RequestBody UserPO user){
        // 重置参数
        user.setPassword(null);
        int result = userService.updateUser(user);
        if (result < 1) {
            return ResultVO.getFailure();
        }
        return ResultVO.getSuccess();
    }

    @PostMapping("/updatePassword")
    @ControllerWebLog(content = "/system/user/resetPassword", actionType = ActionType.UPDATE, intoDb = true)
    public ResultVO resetPassword(@RequestBody JSONObject jsonObject){
        String username = jsonObject.getString("username");
        String oldPassword = jsonObject.getString("oldPassword");
        String newPassword = jsonObject.getString("newPassword");
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)) {
            return ResultVO.getFailure(SystemConstants.PARAMETERS_INCOMPLETE);
        }

        EncryptUtil encryptUtil = new EncryptUtil();
        UserPO user = (UserPO) SecurityUtils.getSubject().getPrincipal();
        String principalUsername = user.getUsername();
        // 具有相应角色权限的管理员才可以重置, 本人可以重置自己的密码,无需验证
        if (!username.equals(principalUsername)) {
            // 判断具有相应角色角色才可以更新
            if (userService.checkRoleUpdatedPass()) {
                user = userService.getUserInfo(username);
            } else {
                return ResultVO.getFailure(SystemConstants.ROLE_NOT_CHANGE_PASS);
            }
        }

        String salt = user.getSalt();
        String oldPasswordEncrypt = encryptUtil.encryptedPwd(oldPassword,salt);
        // 判断旧密码是否正确
        if (!oldPasswordEncrypt.equals(user.getPassword())) {
            return ResultVO.getFailure(SystemConstants.ORIGINAL_PWD_WRONG);
        }

        String newPasswordEncrypt = encryptUtil.encryptedPwd(newPassword,salt);
        user.setPassword(newPasswordEncrypt);
        userService.updateUser(user);
        return ResultVO.getSuccess();
    }

    @DeleteMapping("/del/{username}")
    @ControllerWebLog(content = "/system/user/del", actionType = ActionType.DEL, intoDb = true)
    public ResultVO del(@PathVariable String username){
        try {
            int result = userService.deleteUser(username);
            if (result < 1) {
                String msg = ResultConstant.DEFAULT_FAILURE_MSG;
                if (result == -1) {
                    msg = SystemConstants.ROLE_NOT_DELETE_USER;
                }
                return ResultVO.getFailure(msg);
            }
        }catch (SystemException e){
            return ResultVO.getFailure(e.getMessage());
        }
        return ResultVO.getSuccess();
    }

    @GetMapping("/info")
    public ResultVO info(String token){
        if (StringUtils.isEmpty(token)) {
            return ResultVO.getFailure();
        }
        String username = TokenUtil.getUsername(token);
        UserDTO userInfo = userService.getUserAndRoles(username);
        if (userInfo == null) {
            return ResultVO.getFailure(50016, SystemConstants.USER_EMPTY);
        }
        userInfo.getRoles().clear();
        List<MenuVO> routes = menuService.getRoutes();
        userInfo.setRoutes(routes);
        return ResultVO.getSuccess(userInfo);
    }

    @PostMapping("/updateUserRole")
    @ControllerWebLog(content = "/system/user/updateUserRole", actionType = ActionType.AUTH, intoDb = true)
    public ResultVO updateUserRole(@RequestBody UserDTO userDTO){
        userService.updateUserRole(userDTO);
        return ResultVO.getSuccess();
    }
}
