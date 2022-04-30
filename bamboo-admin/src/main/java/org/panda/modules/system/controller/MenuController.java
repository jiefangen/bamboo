package org.panda.modules.system.controller;

import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.panda.common.constant.SystemConstants;
import org.panda.common.domain.ResultConstant;
import org.panda.common.domain.ResultVO;
import org.panda.common.exception.SystemException;
import org.panda.modules.system.domain.dto.RoleDTO;
import org.panda.modules.system.domain.po.MenuPO;
import org.panda.modules.system.domain.po.RolePO;
import org.panda.modules.system.service.MenuService;
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
    public ResultVO add(@RequestBody MenuPO menu){
        menuService.addMenu(menu);
        return ResultVO.getSuccess();
    }

    @PutMapping("/update")
    public ResultVO update(@RequestBody MenuPO menu){
        menuService.updateMenu(menu);
        return ResultVO.getSuccess();
    }

    @DeleteMapping("/del/{menuId}")
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
