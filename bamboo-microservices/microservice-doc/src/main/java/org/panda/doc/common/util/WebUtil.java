package org.panda.doc.common.util;

import javax.servlet.http.HttpServletResponse;

/**
 * Web工具类
 *
 * @author fangen
 **/
public class WebUtil {

    private WebUtil() {
    }

    public static void setFileResponse(HttpServletResponse response, String filename) {
        // 设置响应内容类型
        response.setContentType("application/octet-stream");
        // 设置响应头，告诉浏览器将响应内容作为附件下载
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
    }

}
