package org.panda.business.admin.modules.system.service.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.panda.business.admin.modules.system.api.vo.MenuVO;
import org.panda.business.admin.modules.system.service.dto.SysMenuDto;
import org.panda.business.admin.modules.system.service.entity.SysMenu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 系统菜单 Mapper 接口
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-24
 */
@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    /**
     * 查询指定树节点的所有子孙节点
     *
     * @param parentId 父级ID
     * @return 指定节点内的树
     */
    List<SysMenuDto> findChildByParentId(@Param("parentId") Integer parentId);

    /**
     * 查询指定路由节点的所有子孙节点--不包含自身节点
     *
     * @param parentId 父级ID
     * @return 指定节点内的树
     */
    List<MenuVO> findRouteByParentId(@Param("parentId") Integer parentId);

    /**
     * 查询路由节点的角色集
     *
     * @param id 菜单ID
     * @return 指定节点的角色集
     */
    List<String> findRoleByMenuId(@Param("id") Integer id);

    /**
     * 查询指定节点以及指定子孙节点--包含自身节点
     *
     * @param menuId 节点唯一ID
     * @return 指定节点树
     */
    List<MenuVO> findSelfAndRouteById(@Param("menuId") Integer menuId);

    /**
     * 查询该指定角色拥有的资源权限
     *
     * @param idAndRoleId
     * @return
     */
    List<MenuVO> findRouteByRoleId(@Param("idAndRoleId") String idAndRoleId);

    /**
     * 查询父节点ID
     *
     * @param id 节点ID
     * @return
     */
    Integer findParentById(@Param("id") Integer id);

    /**
     * 更新该角色的菜单权限
     *
     * @param roleId
     * @param menuIds
     */
    void updateRoleRoutes(@Param("roleId") Integer roleId, @Param("menuIds") List<Integer> menuIds);

    void insertMenu(@Param("menu") SysMenu menu);

    int deleteMenu(@Param("menuId") Integer menuId);

    Boolean delMenuVerify(@Param("menuId") Integer menuId);

    int updateMenu(@Param("menu") SysMenu menu);
}
