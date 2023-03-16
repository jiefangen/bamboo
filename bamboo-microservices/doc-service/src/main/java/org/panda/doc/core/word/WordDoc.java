package org.panda.doc.core.word;

import org.apache.commons.io.IOUtils;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.panda.doc.common.DocConstant;
import org.panda.doc.core.model.DocModel;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Word文档
 */
public class WordDoc implements Word {

    @Override
    public String read(InputStream inputStream, String extension) {
        StringBuilder content = new StringBuilder();
        try {
            if (DocConstant.WORD_DOC.equalsIgnoreCase(extension)) {
                // 使用HWPF API读取DOC格式的文档内容
            } else if (DocConstant.WORD_DOCX.equalsIgnoreCase(extension)) {
                XWPFDocument document = new XWPFDocument(inputStream);
                XWPFWordExtractor extractor = new XWPFWordExtractor(document);
                content.append(extractor.getText());
                extractor.close();
                document.close();
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


}
