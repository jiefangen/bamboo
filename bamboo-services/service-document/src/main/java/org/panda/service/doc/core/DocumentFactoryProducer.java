package org.panda.service.doc.core;

import org.panda.service.doc.common.DocConstants;
import org.panda.service.doc.core.utils.DocFactoryUtils;
import org.panda.service.doc.core.domain.factory.excel.Excel;
import org.panda.service.doc.core.domain.factory.pdf.Pdf;
import org.panda.service.doc.core.domain.factory.ppt.Ppt;
import org.panda.service.doc.core.domain.factory.word.Word;

/**
 * 文档工厂生成器
 */
public class DocumentFactoryProducer {

    public static Excel getExcelDoc() {
        return (Excel) DocFactoryUtils.getDocument(DocConstants.EXCEL);
    }

    public static Word getWordDoc() {
        return (Word) DocFactoryUtils.getDocument(DocConstants.WORD);
    }

    public static Ppt getPptDoc() {
        return (Ppt) DocFactoryUtils.getDocument(DocConstants.PPT);
    }

    public static Pdf getPdfDoc() {
        return (Pdf) DocFactoryUtils.getDocument(DocConstants.PDF);
    }
}

