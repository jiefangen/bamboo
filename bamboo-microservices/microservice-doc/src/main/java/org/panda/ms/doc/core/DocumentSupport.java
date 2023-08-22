package org.panda.ms.doc.core;

import org.panda.ms.doc.core.domain.DocFactory;
import org.panda.ms.doc.core.domain.factory.excel.Excel;
import org.panda.ms.doc.core.domain.factory.ppt.Ppt;
import org.panda.ms.doc.core.domain.factory.word.Word;
import org.panda.ms.doc.common.DocConstants;

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

