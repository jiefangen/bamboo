package org.panda.business.admin.modules.system.controller;

import io.swagger.annotations.Api;
import org.panda.business.admin.common.constant.annotation.ControllerWebLog;
import org.panda.business.admin.common.constant.enumeration.ActionType;
import org.panda.business.admin.common.model.ResultConstant;
import org.panda.business.admin.common.model.ResultVO;
import org.panda.business.admin.common.exception.SystemException;
import org.panda.business.admin.modules.system.model.po.MenuPO;
import org.panda.business.admin.modules.system.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

/**
 * 菜单管理
 *
 * @author fangen
 * @since JDK 11 2022/4/16
 */
@Api(tags = "系统菜单管理")
@RestController
@RequestMapping("/auth/system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/getMenus")
    @ControllerWebLog(content = "/system/menu/getMenus", actionType = ActionType.QUERY)
    public ResultVO getMenus(){
        List<MenuPO> routes = menuService.getMenus();
        return ResultVO.getSuccess(routes);
    }

    @GetMapping("/getChildKeys")
    public ResultVO getChildKeys(BigInteger menuId){
        List<BigInteger> childKeys = menuService.getChildKeys(menuId);
        Collections.reverse(childKeys);
        return ResultVO.getSuccess(childKeys);
    }

    @PostMapping("/add")
    @ControllerWebLog(content = "/system/menu/add", actionType = ActionType.ADD, intoDb = true)
    public ResultVO add(@RequestBody MenuPO menu){
        menuService.addMenu(menu);
        return ResultVO.getSuccess();
    }

    @PutMapping("/update")
    @ControllerWebLog(content = "/system/menu/update", actionType = ActionType.UPDATE, intoDb = true)
    public ResultVO update(@RequestBody MenuPO menu){
        menuService.updateMenu(menu);
        return ResultVO.getSuccess();
    }

    @DeleteMapping("/del/{menuId}")
    @ControllerWebLog(content = "/system/menu/del", actionType = ActionType.DEL, intoDb = true)
    public ResultVO del(@PathVariable BigInteger menuId){
        try {
            int result = menuService.deleteMenu(menuId);
            if (result < 1) {
                return ResultVO.getFailure(ResultConstant.DEFAULT_FAILURE_MSG);
            }
        }catch (SystemException e){
            return ResultVO.getFailure(e.getMessage());
        }
        return ResultVO.getSuccess();
    }
}
