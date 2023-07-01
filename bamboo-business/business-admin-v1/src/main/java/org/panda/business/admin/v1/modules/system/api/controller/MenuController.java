package org.panda.business.admin.v1.modules.system.api.controller;

import io.swagger.annotations.Api;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.business.admin.v1.common.constant.Authority;
import org.panda.business.admin.v1.modules.system.service.SysMenuService;
import org.panda.business.admin.v1.modules.system.service.dto.SysMenuDto;
import org.panda.business.admin.v1.modules.system.service.entity.SysMenu;
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
    public RestfulResult add(@RequestBody SysMenu menu){
        menuService.addMenu(menu);
        return RestfulResult.success();
    }

    @PutMapping("/update")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "system_menu_update")
    })
    public RestfulResult update(@RequestBody SysMenu menu){
        menuService.updateMenu(menu);
        return RestfulResult.success();
    }

    @DeleteMapping("/del/{menuId}")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "system_menu_del")
    })
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