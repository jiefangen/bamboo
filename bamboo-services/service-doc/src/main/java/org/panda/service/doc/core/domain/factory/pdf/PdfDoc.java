package org.panda.service.doc.core.domain.factory.pdf;

import org.apache.commons.io.IOUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.service.doc.core.domain.factory.pdf.helper.PdfDocHelper;
import org.panda.service.doc.core.domain.document.DocModel;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * PDF文档
 */
public class PdfDoc implements Pdf {

    @Override
    public String read(InputStream inputStream, String extension) {
        String content = Strings.EMPTY;
        try {
            PdfDocHelper pdfDocHelper = new PdfDocHelper(inputStream);
            content = pdfDocHelper.getText();
            pdfDocHelper.close();
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
