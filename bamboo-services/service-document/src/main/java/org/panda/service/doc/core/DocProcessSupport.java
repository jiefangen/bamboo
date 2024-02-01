package org.panda.service.doc.core;

import org.panda.service.doc.common.DocConstants;
import org.panda.service.doc.core.domain.DocFactory;
import org.panda.service.doc.core.domain.factory.excel.Excel;
import org.panda.service.doc.core.domain.factory.pdf.Pdf;
import org.panda.service.doc.core.domain.factory.ppt.Ppt;
import org.panda.service.doc.core.domain.factory.word.Word;

/**
 * 抽象文档处理支持
 */
public abstract class DocProcessSupport {

    protected Excel getExcelDoc() {
        return (Excel) DocFactory.getDocument(DocConstants.EXCEL);
    }

    protected Word getWordDoc() {
        return (Word) DocFactory.getDocument(DocConstants.WORD);
    }

    protected Ppt getPptDoc() {
        return (Ppt) DocFactory.getDocument(DocConstants.PPT);
    }

    protected Pdf getPdfDoc() {
        return (Pdf) DocFactory.getDocument(DocConstants.PDF);
    }
}

