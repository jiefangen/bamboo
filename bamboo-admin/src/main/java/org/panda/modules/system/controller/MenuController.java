package org.panda.modules.system.controller;

import io.swagger.annotations.Api;
import org.panda.common.domain.ResultVO;
import org.panda.modules.system.domain.po.MenuPO;
import org.panda.modules.system.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
