package org.panda.tech.core.web.context.function;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.tech.core.web.context.WebContextResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

/**
 * 基于Web项目根目录的文件是否存在的断言
 */
@Component
public class WebContextPathPredicate implements Predicate<String> {

    @Autowired
    private WebMvcProperties mvcProperties;

    @Override
    public boolean test(String path) {
        if (StringUtils.isNotBlank(path)) {
            WebMvcProperties.View viewProperties = this.mvcProperties.getView();
            if (!path.startsWith(Strings.SLASH)) {
                path = Strings.SLASH + path;
            }
            path = viewProperties.getPrefix() + path + viewProperties.getSuffix();
            path = path.replaceAll(Strings.DOUBLE_SLASH, Strings.SLASH);
            try {
                WebContextResource resource = new WebContextResource(path);
                return resource.exists();
            } catch (RuntimeException ignored) {
            }
        }
        return false;
    }

}
