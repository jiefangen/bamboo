package org.panda.tech.core.web.context;

import org.panda.bamboo.common.constant.basic.Strings;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ContextResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * 相对于Web项目根目录的上下文资源
 */
public class WebContextResource implements ContextResource {

    /**
     * 相对于工程源代码目录的Web项目根目录，业务工程可根据实际情况进行修改
     */
    public static String WEB_CONTEXT_SRC = "/src/main/webapp";
    private static final String WEB_INF = "WEB-INF";

    private FileSystemResource file;
    private String path;

    public WebContextResource(String path) {
        try {
            File classpathParentDir = new ClassPathResource(Strings.SLASH).getFile().getParentFile();
            File contextDir = classpathParentDir.getParentFile();
            if (!WEB_INF.equals(classpathParentDir.getName())) {
                // 类路径父目录不是WEB-INF，说明当前是开发环境，需找到源代码中的Web项目根目录
                contextDir = new File(contextDir, WEB_CONTEXT_SRC);
            }
            String contextRoot = contextDir.getAbsolutePath();
            contextRoot = StringUtils.cleanPath(contextRoot);
            this.path = StringUtils.cleanPath(path);
            this.file = new FileSystemResource(contextRoot + Strings.SLASH + this.path);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String getPathWithinContext() {
        return this.path;
    }

    @Override
    public Resource createRelative(String relativePath) throws IOException {
        return this.file.createRelative(relativePath);
    }

    @Override
    public boolean exists() {
        return this.file.exists();
    }

    @Override
    public String getDescription() {
        return this.file.getDescription();
    }

    @Override
    public File getFile() throws IOException {
        return this.file.getFile();
    }

    @Override
    public String getFilename() {
        return this.file.getFilename();
    }

    @Override
    public URI getURI() throws IOException {
        return this.file.getURI();
    }

    @Override
    public URL getURL() throws IOException {
        return this.file.getURL();
    }

    @Override
    public boolean isOpen() {
        return this.file.isOpen();
    }

    @Override
    public boolean isReadable() {
        return this.file.isReadable();
    }

    @Override
    public long lastModified() throws IOException {
        return this.file.lastModified();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return this.file.getInputStream();
    }

    @Override
    public long contentLength() throws IOException {
        return this.file.contentLength();
    }

}
