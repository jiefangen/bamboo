package org.panda.business.admin.modules.system.api.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * 菜单路由视图--适配前端页面
 *
 * @author jiefangen
 */
@Setter
@Getter
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class MenuVO implements Serializable {
    private static final long serialVersionUID = 914706026276732618L;

    private Integer id;

    private String path;

    private String component;

    private String redirect;

    private String name;

    private Boolean hidden;
    /**
     * 菜单树结构展示父级目录必须字段
     */
    private Boolean alwaysShow;

    private MenuMeta meta;

    LinkedList<MenuVO> children;

    public Boolean getAlwaysShow() {
        // 菜单结构中只有目录菜单才需要跳转路径
        if (StringUtils.isNotBlank(this.redirect)) {
            return Boolean.TRUE;
        }
        return alwaysShow;
    }
}
