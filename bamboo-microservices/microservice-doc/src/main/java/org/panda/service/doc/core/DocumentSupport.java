package org.panda.service.doc.core;

import org.panda.service.doc.common.DocConstants;
import org.panda.service.doc.core.domain.DocFactory;
import org.panda.service.doc.core.domain.factory.excel.Excel;
import org.panda.service.doc.core.domain.factory.ppt.Ppt;
import org.panda.service.doc.core.domain.factory.word.Word;

public abstract class DocumentSupport {

    protected Excel getExcelDoc() {
        return (Excel) DocFactory.getDocument(DocConstants.EXCEL);
    }

    protected Word getWordDoc() {
        return (Word) DocFactory.getDocument(DocConstants.WORD);
    }

    protected Ppt getPptDoc() {
        return (Ppt) DocFactory.getDocument(DocConstants.PPT);
    }
}

