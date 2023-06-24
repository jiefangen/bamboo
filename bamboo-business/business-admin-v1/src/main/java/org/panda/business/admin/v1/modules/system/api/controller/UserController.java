package org.panda.business.admin.v1.modules.system.api.controller;

import io.swagger.annotations.Api;
import org.panda.business.admin.v1.common.constant.AuthConstants;
import org.panda.business.admin.v1.modules.system.api.param.UserQueryParam;
import org.panda.business.admin.v1.modules.system.api.vo.UserVO;
import org.panda.business.admin.v1.modules.system.service.ISysUserService;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.security.config.annotation.ConfigAnonymous;
import org.panda.tech.security.config.annotation.ConfigAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户管理
 *
 * @author jiefangen
 **/
@Api(tags = "系统用户管理")
@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private ISysUserService userService;

    @PostMapping("/page")
    @ConfigAnonymous
    public RestfulResult page(@RequestBody UserQueryParam queryParam){
        QueryResult<UserVO> userPage = userService.getUserByPage(queryParam);
        return RestfulResult.success(userPage);
    }

    @GetMapping("/info")
    @ConfigAuthority
    public RestfulResult info(HttpServletRequest request){
        String token = request.getHeader(WebConstants.HEADER_AUTH_JWT);
        UserVO userInfo = userService.getUserByToken(token);
        if (userInfo == null) {
            return RestfulResult.failure(AuthConstants.USER_NOT_EXIST_CODE, AuthConstants.USERNAME_NOT_EXIST);
        }
        return RestfulResult.success(userInfo);
    }

//    @PostMapping("/add")
//    public RestfulResult add(@RequestBody UserPO user){
//        String username = user.getUsername();
//        String password = user.getPassword();
//        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
//            return ResultVO.getFailure(SystemConstants.PARAMETERS_INCOMPLETE);
//        }
//        EncryptUtil encryptUtil = new EncryptUtil();
//        encryptUtil.encryptedPwd(user);
//        String result = userService.addUser(user);
//        if (!ResultConstant.DEFAULT_SUCCESS_MSG.equals(result)) {
//            return ResultVO.getFailure(result);
//        }
//
//        return ResultVO.getSuccess();
//    }
//
//    @PutMapping("/edit")
//    public RestfulResult edit(@RequestBody UserPO user){
//        // 重置参数
//        user.setPassword(null);
//        int result = userService.updateUser(user);
//        if (result < 1) {
//            return ResultVO.getFailure();
//        }
//        return ResultVO.getSuccess();
//    }
//
//    @PostMapping("/updatePassword")
//    public RestfulResult resetPassword(@RequestBody JSONObject jsonObject){
//        String username = jsonObject.getString("username");
//        String oldPassword = jsonObject.getString("oldPassword");
//        String newPassword = jsonObject.getString("newPassword");
//        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)) {
//            return ResultVO.getFailure(SystemConstants.PARAMETERS_INCOMPLETE);
//        }
//
//        EncryptUtil encryptUtil = new EncryptUtil();
//        UserPO user = (UserPO) SecurityUtils.getSubject().getPrincipal();
//        String principalUsername = user.getUsername();
//        // 具有相应角色权限的管理员才可以重置, 本人可以重置自己的密码,无需验证
//        if (!username.equals(principalUsername)) {
//            // 判断具有相应角色角色才可以更新
//            if (userService.checkRoleUpdatedPass()) {
//                user = userService.getUserInfo(username);
//            } else {
//                return ResultVO.getFailure(SystemConstants.ROLE_NOT_CHANGE_PASS);
//            }
//        }
//
//        String salt = user.getSalt();
//        String oldPasswordEncrypt = encryptUtil.encryptedPwd(oldPassword,salt);
//        // 判断旧密码是否正确
//        if (!oldPasswordEncrypt.equals(user.getPassword())) {
//            return ResultVO.getFailure(SystemConstants.ORIGINAL_PWD_WRONG);
//        }
//
//        String newPasswordEncrypt = encryptUtil.encryptedPwd(newPassword,salt);
//        user.setPassword(newPasswordEncrypt);
//        userService.updateUser(user);
//        return ResultVO.getSuccess();
//    }
//
//    @DeleteMapping("/del/{username}")
//    @ControllerWebLog(content = "/system/user/del", actionType = ActionType.DEL, intoDb = true)
//    public RestfulResult del(@PathVariable String username){
//        try {
//            int result = userService.deleteUser(username);
//            if (result < 1) {
//                String msg = ResultConstant.DEFAULT_FAILURE_MSG;
//                if (result == -1) {
//                    msg = SystemConstants.ROLE_NOT_DELETE_USER;
//                }
//                return ResultVO.getFailure(msg);
//            }
//        }catch (SystemException e){
//            return ResultVO.getFailure(e.getMessage());
//        }
//        return ResultVO.getSuccess();
//    }
//

//
//    @PostMapping("/updateUserRole")
//    public RestfulResult updateUserRole(@RequestBody UserDTO userDTO){
//        userService.updateUserRole(userDTO);
//        return ResultVO.getSuccess();
//    }
}
