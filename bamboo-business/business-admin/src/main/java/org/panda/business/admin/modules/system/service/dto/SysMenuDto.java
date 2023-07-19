package org.panda.business.admin.modules.system.service.dto;

import lombok.Getter;
import lombok.Setter;
import org.panda.business.admin.modules.system.service.entity.SysMenu;

import java.io.Serializable;
import java.util.LinkedList;

@Setter
@Getter
public class SysMenuDto implements Serializable {
    private static final long serialVersionUID = -2042410581476113356L;

    private Integer id;

    private Integer parentId;

    private String menuPath;

    private String component;

    private String menuName;

    private String redirect;

    private String title;

    private String icon;

    private Boolean hidden;

    private Integer sort;

    private LinkedList<SysMenu> children;
}
