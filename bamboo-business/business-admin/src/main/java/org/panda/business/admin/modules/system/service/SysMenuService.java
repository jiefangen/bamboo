package org.panda.business.admin.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.business.admin.modules.system.api.vo.MenuVO;
import org.panda.business.admin.modules.system.service.dto.SysMenuDto;
import org.panda.business.admin.modules.system.service.entity.SysMenu;

import java.util.List;

/**
 * <p>
 * 系统菜单 服务类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-24
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenuDto> getMenus();

    List<MenuVO> getRoutes();

    List<Integer> getChildKeys(Integer menuId);

    void addMenu(SysMenu menu);

    int updateMenu(SysMenu menu);

    int deleteMenu(Integer menuId) throws BusinessException;
}
