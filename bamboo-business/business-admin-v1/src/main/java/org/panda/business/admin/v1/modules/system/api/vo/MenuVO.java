package org.panda.business.admin.v1.modules.system.api.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

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

    private MenuMeta meta;

    LinkedList<MenuVO> children;
}