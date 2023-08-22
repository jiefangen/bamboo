package org.panda.ms.doc.core.domain;

import org.panda.ms.doc.core.domain.factory.Document;
import org.panda.ms.doc.core.domain.factory.excel.ExcelDoc;
import org.panda.ms.doc.core.domain.factory.ppt.PptDoc;
import org.panda.ms.doc.core.domain.factory.word.WordDoc;
import org.panda.ms.doc.common.DocConstants;

/**
 * 操作文档工厂
 *
 * @author fangen
 **/
public class DocFactory {

    public static Document getDocument(String docType){
        if(DocConstants.EXCEL.equals(docType)){
            return new ExcelDoc();
        } else if(DocConstants.WORD.equals(docType)){
            return new WordDoc();
        } else if(DocConstants.PPT.equals(docType)){
            return new PptDoc();
        }
        return null;
    }
}
