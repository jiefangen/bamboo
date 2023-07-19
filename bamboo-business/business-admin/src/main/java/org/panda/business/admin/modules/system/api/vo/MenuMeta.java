package org.panda.business.admin.modules.system.api.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 菜单资源元属性
 *
 * @author fangen
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
