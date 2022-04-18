package org.panda.modules.system.domain.po;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.SortedSet;

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

    private String meta;

    private String component;

    private Boolean hidden;

    private Integer sort;

    LinkedList<MenuPO> children;
}
