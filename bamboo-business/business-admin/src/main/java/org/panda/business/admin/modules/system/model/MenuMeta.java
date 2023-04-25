package org.panda.business.admin.modules.system.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 菜单资源属性
 *
 * @author fangen
 * @since JDK 11 2022/4/18
 */
@Setter
@Getter
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class MenuMeta {
    private static final long serialVersionUID = 1278642021650652876L;

    private String title;

    private String icon;

    private List<String> roles;
}
