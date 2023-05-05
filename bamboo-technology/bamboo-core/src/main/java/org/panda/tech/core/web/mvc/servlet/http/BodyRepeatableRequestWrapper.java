package org.panda.tech.core.web.mvc.servlet.http;

import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 可重复读取body的请求包装器
 */
public class BodyRepeatableRequestWrapper extends HttpServletRequestWrapper {

    private byte[] body;

    public BodyRepeatableRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (this.body == null) {
            this.body = IOUtils.toByteArray(super.getInputStream());
        }
        InputStream in = new ByteArrayInputStream(this.body);
        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return in.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }
}
