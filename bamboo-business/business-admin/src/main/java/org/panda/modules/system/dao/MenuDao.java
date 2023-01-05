package org.panda.modules.system.dao;

import org.apache.ibatis.annotations.Param;
import org.panda.modules.system.domain.po.MenuPO;
import org.panda.modules.system.domain.vo.MenuVO;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface MenuDao {

    /**
     * 查询指定树节点的所有子孙节点
     *
     * @param parentId 父级ID
     * @return 指定节点内的树
     */
    List<MenuPO> findChildByParentId(@Param("parentId") BigInteger parentId);

    /**
     * 查询指定路由节点的所有子孙节点--不包含自身节点
     *
     * @param parentId 父级ID
     * @return 指定节点内的树
     */
    List<MenuVO> findRouteByParentId(@Param("parentId") BigInteger parentId);

    /**
     * 查询路由节点的角色集
     *
     * @param id 菜单ID
     * @return 指定节点的角色集
     */
    List<String> findRoleByMenuId(@Param("id") BigInteger id);

    /**
     * 查询指定节点以及指定子孙节点--包含自身节点
     *
     * @param menuId 节点唯一ID
     * @return 指定节点树
     */
    List<MenuVO> findSelfAndRouteById(@Param("menuId") BigInteger menuId);

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
    BigInteger findParentById(@Param("id") BigInteger id);

    /**
     * 更新该角色的菜单权限
     *
     * @param roleId
     * @param menuIds
     */
    void updateRoleRoutes(@Param("roleId") BigInteger roleId, @Param("menuIds") List<BigInteger> menuIds);

    void insertMenu(@Param("menu") MenuPO menu);

    int deleteMenu(@Param("menuId") BigInteger menuId);

    Boolean delMenuVerify(@Param("menuId") BigInteger menuId);

    int updateMenu(@Param("menu") MenuPO menu);
}
