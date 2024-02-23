package org.panda.service.doc.core;

import org.panda.service.doc.common.DocConstants;
import org.panda.service.doc.core.util.DocFactoryUtil;
import org.panda.service.doc.core.domain.factory.excel.Excel;
import org.panda.service.doc.core.domain.factory.pdf.Pdf;
import org.panda.service.doc.core.domain.factory.ppt.Ppt;
import org.panda.service.doc.core.domain.factory.word.Word;

/**
 * 文档工厂生成器
 */
public class DocumentFactoryProducer {

    public static Excel getExcelDoc() {
        return (Excel) DocFactoryUtil.getDocument(DocConstants.EXCEL);
    }

    public static Word getWordDoc() {
        return (Word) DocFactoryUtil.getDocument(DocConstants.WORD);
    }

    public static Ppt getPptDoc() {
        return (Ppt) DocFactoryUtil.getDocument(DocConstants.PPT);
    }

    public static Pdf getPdfDoc() {
        return (Pdf) DocFactoryUtil.getDocument(DocConstants.PDF);
    }
}

