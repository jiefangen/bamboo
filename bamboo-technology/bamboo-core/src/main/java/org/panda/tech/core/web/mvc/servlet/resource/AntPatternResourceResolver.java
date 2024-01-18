package org.panda.tech.core.web.mvc.servlet.resource;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

/**
 * 根据Ant样式匹配决定的资源解决器
 */
public class AntPatternResourceResolver extends PathResourceResolver {

    private String pattern;
    private String path;

    public AntPatternResourceResolver(String pattern) {
        this.path = getPath(pattern);
        if (this.path != null) {
            // 资源路径都不以/开头，所以去掉样式中开头的/
            pattern = pattern.substring(1);
            this.pattern = pattern;
        }
    }

    public static boolean supports(String pattern) {
        return pattern != null && pattern.startsWith("/**/");
    }

    /**
     * 获取与指定样式匹配的路径
     *
     * @param pattern 样式
     * @return 匹配路径
     */
    private String getPath(String pattern) {
        if (supports(pattern)) {
            // 去掉开头的/**
            String path = pattern.substring(3);
            // 去掉从*开始的部分
            int index = path.indexOf(Strings.ASTERISK);
            if (index >= 0) {
                path = path.substring(0, index);
            }
            return path;
        }
        return null;
    }

    @Override
    protected Resource getResource(String resourcePath, Resource location) throws IOException {
        if (this.path != null && StringUtil.antPathMatch(resourcePath, this.pattern)) {
            int index = resourcePath.indexOf(this.path);
            if (index > 0) {
                resourcePath = resourcePath.substring(index);
                if (resourcePath.startsWith(Strings.SLASH)) {
                    resourcePath = resourcePath.substring(1);
                }
            }
        }
        return super.getResource(resourcePath, location);
    }
}
