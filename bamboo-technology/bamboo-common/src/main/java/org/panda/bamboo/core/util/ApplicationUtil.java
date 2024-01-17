package org.panda.bamboo.core.util;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.constant.file.FileExtensions;
import org.panda.bamboo.common.util.io.IOUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 基于当前框架的应用的工具类
 */
public class ApplicationUtil {

    public static final String JAR_URL_PREFIX = ResourceUtils.JAR_URL_PREFIX + ResourceUtils.FILE_URL_PREFIX;
    private static final String JAR_URL_SUFFIX = "ar!";
    /**
     * 是否通过tomcat运行
     * false：以一般应用的方式运行（即调用main()方法）
     */
    public static final boolean VIA_TOMCAT = System.getProperty("catalina.home") != null;
    public static final boolean IN_CODING;

    private ApplicationUtil() {
    }

    static {
        try {
            Resource resource = new ClassPathResource(Strings.SLASH);
            String classPath = resource.getURL().toString().replace('\\', '/');
            IN_CODING = (!classPath.startsWith(JAR_URL_PREFIX))
                    && (StringUtil.antPathMatch(classPath, "**/target/**/classes/")
                    || classPath.endsWith("/target/test-classes/"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return 应用运行时所在的目录
     */
    public static File getWorkingDir() {
        Resource resource = new ClassPathResource(Strings.SLASH);
        try {
            if (VIA_TOMCAT) {
                if (IN_CODING) { // 编程时通过Tomcat运行
                    // 形如：**/target/[构建名称]/WEB-INF/classes
                    return resource.getFile().getParentFile().getParentFile().getParentFile();
                } else { // 构建后通过Tomcat运行
                    // 形如：**/webapps/[构建名称]/WEB-INF/classes
                    return resource.getFile().getParentFile().getParentFile().getParentFile();
                }
            } else {
                if (IN_CODING) { // 编程时以一般应用的方式运行
                    // 形如：**/target/classes
                    return resource.getFile().getParentFile();
                } else { // 构建后以一般应用的方式运行
                    // 形如：jar:file:/**/[构建名称].war!/WEB-INF/classes!/
                    String location = resource.getURL().toString().replace('\\', '/');
                    location = location.substring(JAR_URL_PREFIX.length(), location.indexOf(JAR_URL_SUFFIX));
                    location = location.substring(0, location.lastIndexOf(Strings.SLASH));
                    return new File(location);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return 应用根目录
     */
    public static File getApplicationRootDir() {
        return getWorkingDir().getParentFile();
    }

    /**
     * 获取工作临时目录，与工作目录相关，非系统临时目录
     *
     * @return 工作临时目录
     */
    public static File getWorkingTempDir() {
        return new File(getApplicationRootDir(), "temp");
    }

    public static String getAbsolutePath(String path) {
        if (path.startsWith(Strings.DOT)) { // 相对路径
            File root = getApplicationRootDir();
            if (path.startsWith("./")) {
                path = root.getAbsolutePath() + IOUtil.FILE_SEPARATOR + path.substring(2);
            } else if (path.startsWith("../")) {
                path = root.getParent() + IOUtil.FILE_SEPARATOR + path.substring(3);
            } else {
                throw new RuntimeException("Invalid root path: " + path);
            }
        }
        return path;
    }

    public static File getWarFile() {
        try {
            if (IN_CODING) {
                // 从maven的pom.xml文件中读取构建目标文件
                File root = ApplicationUtil.getApplicationRootDir();
                File pomFile = new File(root, "pom.xml");
                SAXReader reader = new SAXReader();
                Document doc = reader.read(pomFile);
                Element projectElement = doc.getRootElement();
                Element buildElement = projectElement.element("build");
                String buildName = buildElement.elementTextTrim("finalName");
                String targetDirLocation = getTargetDirLocation(buildElement);
                File targetDir;
                if (targetDirLocation != null) {
                    while (targetDirLocation.startsWith("../")) {
                        targetDirLocation = targetDirLocation.substring(3);
                        root = root.getParentFile();
                    }
                    targetDir = new File(root, targetDirLocation);
                } else {
                    targetDir = getWorkingDir();
                }
                return new File(targetDir, buildName + FileExtensions.DOT_WAR);
            }
            Resource resource = new ClassPathResource(Strings.SLASH);
            if (VIA_TOMCAT) {
                // 形如：**/webapps/[构建名称]/WEB-INF/classes
                return new File(resource.getFile().getParentFile().getParent() + FileExtensions.DOT_WAR);
            } else {
                // 形如：jar:file:/**/[构建名称].war!/WEB-INF/classes!/
                String location = resource.getURL().toString().replace('\\', '/');
                location = location.substring(JAR_URL_PREFIX.length(), location.indexOf(JAR_URL_SUFFIX) + 2);
                return new File(location);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getTargetDirLocation(Element buildElement) {
        Element pluginsElement = buildElement.element("plugins");
        if (pluginsElement != null) {
            List<Element> pluginElements = pluginsElement.elements("plugin");
            for (Element pluginElement : pluginElements) {
                String groupId = pluginElement.elementTextTrim("groupId");
                String artifactId = pluginElement.elementTextTrim("artifactId");
                if ("org.springframework.boot".equals(groupId) && "spring-boot-maven-plugin".equals(artifactId)) {
                    Element configurationElement = pluginElement.element("configuration");
                    return configurationElement == null ? null :
                            configurationElement.elementTextTrim("outputDirectory");
                }
            }
        }
        return null;
    }

}
