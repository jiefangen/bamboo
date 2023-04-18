package org.panda.service.doc.core.ppt;

import org.apache.commons.io.IOUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.panda.service.doc.common.DocConstants;
import org.panda.service.doc.core.domain.DocModel;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * PPT文档
 */
public class PptDoc implements Ppt {

    @Override
    public String read(InputStream inputStream, String extension) {
        StringBuilder content = new StringBuilder();
        try {
            if (DocConstants.PPT_PPT.equalsIgnoreCase(extension)) {
                // 使用HSLF API读取PPT格式的文档内容
            } else if (DocConstants.PPT_PPTX.equalsIgnoreCase(extension)) {
                XMLSlideShow slideShow = new XMLSlideShow(inputStream);
                for (XSLFSlide slide : slideShow.getSlides()) {
                    for (XSLFTextShape shape : slide.getPlaceholders()) {
                        content.append(shape.getText()).append("\n");
                    }
                }
                slideShow.close();
            }
        } catch (IOException e) {
            // do nothing
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return content.toString();
    }

    @Override
    public void create(DocModel docModel, ServletOutputStream outputStream) {

    }

    @Override
    public void preview(ServletOutputStream outputStream) {

    }


}
