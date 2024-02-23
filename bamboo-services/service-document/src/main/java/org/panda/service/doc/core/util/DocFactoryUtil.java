package org.panda.service.doc.core.util;

import org.panda.service.doc.common.DocConstants;
import org.panda.service.doc.core.domain.factory.Document;
import org.panda.service.doc.core.domain.factory.excel.ExcelDoc;
import org.panda.service.doc.core.domain.factory.pdf.PdfDoc;
import org.panda.service.doc.core.domain.factory.ppt.PptDoc;
import org.panda.service.doc.core.domain.factory.word.WordDoc;

/**
 * 操作文档工厂
 *
 * @author fangen
 **/
public class DocFactoryUtil {

    public static Document getDocument(String docType){
        if(DocConstants.EXCEL.equalsIgnoreCase(docType)){
            return new ExcelDoc();
        } else if(DocConstants.WORD.equalsIgnoreCase(docType)){
            return new WordDoc();
        } else if(DocConstants.PPT.equalsIgnoreCase(docType)){
            return new PptDoc();
        } else if(DocConstants.PDF.equalsIgnoreCase(docType)){
            return new PdfDoc();
        }
        return null;
    }
}
