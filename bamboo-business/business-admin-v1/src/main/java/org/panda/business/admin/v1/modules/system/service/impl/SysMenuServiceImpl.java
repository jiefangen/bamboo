package org.panda.business.admin.v1.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.business.admin.v1.modules.system.api.vo.MenuVO;
import org.panda.business.admin.v1.modules.system.service.SysMenuService;
import org.panda.business.admin.v1.modules.system.service.dto.SysMenuDto;
import org.panda.business.admin.v1.modules.system.service.entity.SysMenu;
import org.panda.business.admin.v1.modules.system.service.repository.SysMenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 系统菜单 服务实现类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-24
 */
@Service
@Transactional
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private static final int ZERO = 0;

    @Override
    public List<SysMenuDto> getMenus() {
        return this.baseMapper.findChildByParentId(ZERO);
    }

    @Override
    public List<MenuVO> getRoutes() {
        List<MenuVO> routes = this.baseMapper.findRouteByParentId(ZERO);
        return routes;
    }

    private List<Integer> getParentOfChild(List<Integer> childKeys, Integer menuId) {
        Integer parentId = this.baseMapper.findParentById(menuId);
        childKeys.add(menuId);
        if (parentId == null || parentId == 0) {
            return childKeys;
        } else {
            return getParentOfChild(childKeys, parentId);
        }
    }

    @Override
    public List<Integer> getChildKeys(Integer menuId) {
        return getParentOfChild(new LinkedList<>(), menuId);
    }

    @Override
    public void addMenu(SysMenu menu) {
        menu.setId(null); // 使用数据库自增主键ID
        if (menu.getParentId() != null && menu.getParentId() == 0) { // 顶级菜单
            menu.setComponent("layout");
        }
        String menuName = menu.getTitle();
        menu.setMenuName(StringUtil.firstToUpperCase(menuName));
        this.baseMapper.insertMenu(menu);
    }

    @Override
    public int updateMenu(SysMenu menu) {
        return this.baseMapper.updateMenu(menu);
    }

    @Override
    public int deleteMenu(Integer menuId) throws BusinessException {
        // 校验该角色是否绑定的有用户或菜单权限资源
        if (this.baseMapper.delMenuVerify(menuId)) {
            throw new BusinessException("Delete the submenu or unbind the menu role first.");
        }
        return this.baseMapper.deleteMenu(menuId);
    }
}
