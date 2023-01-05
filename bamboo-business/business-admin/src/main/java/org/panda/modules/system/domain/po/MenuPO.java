package org.panda.modules.system.domain.po;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.LinkedList;

/**
 * 菜单表映射对象
 *
 * @author jiefangen
 * @since JDK 11  2022/4/17
 **/
@Setter
@Getter
public class MenuPO implements Serializable{
    private static final long serialVersionUID = 2345642021650652148L;

    private BigInteger id;

    private BigInteger parentId;

    private String menuPath;

    private String redirect;

    private String menuName;

    private String title;

    private String icon;

    private String component;

    private String isHidden;

    private Integer sort;

    LinkedList<MenuPO> children;
}
