package org.panda.business.admin.modules.system.api.controller;

import io.swagger.annotations.Api;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.business.admin.common.constant.Authority;
import org.panda.business.admin.modules.system.api.vo.MenuVO;
import org.panda.business.admin.modules.system.service.SysMenuService;
import org.panda.business.admin.modules.system.service.dto.SysMenuDto;
import org.panda.business.admin.modules.system.service.entity.SysMenu;
import org.panda.tech.core.spec.log.ActionType;
import org.panda.tech.core.web.config.annotation.WebOperationLog;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.security.config.annotation.ConfigAuthorities;
import org.panda.tech.security.config.annotation.ConfigAuthority;
import org.panda.tech.security.config.annotation.ConfigPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * 菜单权限管理
 *
 * @author fangen
 */
@Api(tags = "系统菜单管理")
@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private SysMenuService menuService;

    @GetMapping("/getRoutes")
    @ConfigPermission
    public RestfulResult getRoutes() {
        List<MenuVO> routes = menuService.getRoutes();
        return RestfulResult.success(routes);
    }

    @GetMapping("/getMenus")
    @ConfigPermission
    public RestfulResult getMenus(){
        List<SysMenuDto> routes = menuService.getMenus();
        return RestfulResult.success(routes);
    }

    @GetMapping("/getChildKeys")
    @ConfigPermission
    public RestfulResult getChildKeys(Integer menuId){
        List<Integer> childKeys = menuService.getChildKeys(menuId);
        Collections.reverse(childKeys);
        return RestfulResult.success(childKeys);
    }

    @PostMapping("/add")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "system_menu_add")
    })
    @WebOperationLog(actionType = ActionType.ADD, intoStorage = true)
    public RestfulResult add(@RequestBody SysMenu menu){
        menuService.addMenu(menu);
        return RestfulResult.success();
    }

    @PutMapping("/update")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "system_menu_update")
    })
    @WebOperationLog(actionType = ActionType.UPDATE, intoStorage = true)
    public RestfulResult update(@RequestBody SysMenu menu){
        menuService.updateMenu(menu);
        return RestfulResult.success();
    }

    @DeleteMapping("/del/{menuId}")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "system_menu_del")
    })
    @WebOperationLog(actionType = ActionType.DEL, intoStorage = true)
    public RestfulResult del(@PathVariable Integer menuId){
        try {
            int result = menuService.deleteMenu(menuId);
            if (result < 1) {
                return RestfulResult.failure();
            }
        }catch (BusinessException e){
            return RestfulResult.failure(e.getMessage());
        }
        return RestfulResult.success();
    }
}
