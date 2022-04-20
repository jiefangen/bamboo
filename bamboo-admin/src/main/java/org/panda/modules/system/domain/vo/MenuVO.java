package org.panda.modules.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.panda.modules.system.domain.MenuMeta;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.LinkedList;

/**
 * 菜单路由视图--适配前端页面
 *
 * @author jiefangen
 * @since JDK 11  2022/4/18
 */
@Setter
@Getter
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class MenuVO implements Serializable {
    private static final long serialVersionUID = 1278642021650652148L;

    private BigInteger id;

    private String path;

    private String component;

    private String redirect;

    private String name;

    private Boolean hidden;

    private MenuMeta meta;

    LinkedList<MenuVO> children;
}
