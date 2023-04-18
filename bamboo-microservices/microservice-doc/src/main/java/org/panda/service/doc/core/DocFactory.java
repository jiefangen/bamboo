package org.panda.service.doc.core;

import org.panda.service.doc.common.DocConstants;
import org.panda.service.doc.core.excel.ExcelDoc;
import org.panda.service.doc.core.ppt.PptDoc;
import org.panda.service.doc.core.word.WordDoc;

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
