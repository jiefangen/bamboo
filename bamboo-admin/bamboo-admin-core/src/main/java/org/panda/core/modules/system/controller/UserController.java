package org.panda.core.modules.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.panda.common.domain.ResultVO;
import org.panda.core.common.constant.SystemConstant;
import org.panda.core.common.util.EncrypterUtil;
import org.panda.core.modules.system.domain.param.UserQueryParam;
import org.panda.core.modules.system.domain.po.UserPO;
import org.panda.core.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理
 *
 * @author jiefangen
 * @since JDK 1.8  2020/5/5
 **/
@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public ResultVO info(String username){
        if (StringUtils.isEmpty(username)) {
            return ResultVO.getFailure();
        }
        UserPO userInfo = userService.getUserInfo(username);
        if (userInfo == null) {
            return ResultVO.getFailure(50016, SystemConstant.USER_EMPTY);
        }
        return ResultVO.getSucess(userInfo);
    }

    @GetMapping("/list")
//    @RequiresRoles(value={"SYSTEM","ADMIN"},logical= Logical.OR)
    public ResultVO list(@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize){
        PageHelper.startPage(pageNo,pageSize);
        Page<UserPO> users = userService.getUsers("");
        PageInfo<UserPO> pageInfo = new PageInfo<>(users);
        return ResultVO.getSucess(pageInfo);
    }

    @PostMapping("/page")
//    @RequiresPermissions(value = "permis[get]")
    public PageInfo<UserPO> page(@RequestBody UserQueryParam param){
        param.initPage();
        Page<UserPO> users = userService.getUsers(param.getKeyword());
        PageInfo<UserPO> pageInfo = new PageInfo<>(users);
        return pageInfo;
    }

    @PostMapping("/add")
    public ResultVO add(@RequestBody UserPO user){
        if (user == null) {
            return ResultVO.getFailure();
        }
        String username = user.getUsername();
        String password = user.getPassword();
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return ResultVO.getFailure();
        }

        EncrypterUtil encrypterUtil = new EncrypterUtil();
        encrypterUtil.encrypterPwd(user);
        String msg = userService.addUser(user);
        return ResultVO.getSucess(msg);
    }

    @PostMapping("/updatePassword")
    public ResultVO changePassword(@RequestBody JSONObject jsonObject){
        String oldPassword = jsonObject.getString("oldPassword");
        String newPassword = jsonObject.getString("newPassword");
        if(StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)) {
            return ResultVO.getFailure();
        }

        EncrypterUtil encrypterUtil = new EncrypterUtil();
        UserPO user = (UserPO) SecurityUtils.getSubject().getPrincipal();
        String salt = user.getSalt();
        String oldPasswordEncrypt = encrypterUtil.encrypterPwd(oldPassword,salt);
        // 判断旧密码是否正确
        if (!oldPasswordEncrypt.equals(user.getPassword())) {
            return ResultVO.getFailure(SystemConstant.ORIGINAL_PWD_WRONG);
        }

        String newPasswordEncrypt = encrypterUtil.encrypterPwd(newPassword,salt);
        user.setPassword(newPasswordEncrypt);
        userService.updateUser(user);
        return ResultVO.getSucess();
    }

}
