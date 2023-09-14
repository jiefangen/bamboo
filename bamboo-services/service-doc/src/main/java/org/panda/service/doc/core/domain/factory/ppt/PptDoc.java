package org.panda.service.doc.core.domain.factory.ppt;

import org.apache.commons.io.IOUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.service.doc.core.domain.factory.ppt.helper.PptDocHelper;
import org.panda.service.doc.core.domain.document.DocModel;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * PPT文档
 */
public class PptDoc implements Ppt {

    @Override
    public String read(InputStream inputStream, String extension) {
        String content = Strings.EMPTY;
        try {
            PptDocHelper pptDocHelper = new PptDocHelper(inputStream, extension);
            content = pptDocHelper.getText();
            pptDocHelper.close();
        } catch (Exception e) {
            // do nothing
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return content;
    }

    @Override
    public void create(OutputStream outputStream, DocModel docModel) {
    }

    @Override
    public void convert(InputStream inputStream, OutputStream outputStream, String extension) {
    }

}
