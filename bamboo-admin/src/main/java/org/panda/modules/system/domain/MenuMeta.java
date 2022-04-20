package org.panda.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

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

    private String path;
}
