package org.panda.doc.core;

import org.panda.doc.common.DocConstant;
import org.panda.doc.core.excel.ExcelDoc;
import org.panda.doc.core.ppt.PptDoc;
import org.panda.doc.core.word.WordDoc;

/**
 * 操作文档工厂
 *
 * @author fangen
 **/
public class DocFactory {

    public Document getDocument(String docType){
        if(DocConstant.EXCEL.equalsIgnoreCase(docType)){
            return new ExcelDoc();
        } else if(DocConstant.WORD.equalsIgnoreCase(docType)){
            return new WordDoc();
        }else if(DocConstant.PPT.equalsIgnoreCase(docType)){
            return new PptDoc();
        }
        return null;
    }

}