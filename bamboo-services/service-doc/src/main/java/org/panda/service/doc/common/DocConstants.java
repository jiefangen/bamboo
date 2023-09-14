package org.panda.service.doc.common;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.file.FileExtensions;

import java.util.ArrayList;
import java.util.List;

/**
 * 文档类型常量集
 *
 * @author fangen
 */
public class DocConstants {

    private DocConstants() {
    }

    public static final String EXCEL = "EXCEL";
    public static final String WORD = "WORD";
    public static final String PPT = "PPT";
    public static final String PDF = "PDF";

    public static final String EXCEL_XLS = FileExtensions.XLS;
    public static final String EXCEL_XLSX = FileExtensions.XLSX;
    public static final String WORD_DOC = FileExtensions.DOC;
    public static final String WORD_DOCX = FileExtensions.DOCX;
    public static final String PPT_PPT = FileExtensions.PPT;
    public static final String PPT_PPTX = FileExtensions.PPTX;
    public static final String PDF_PDF = FileExtensions.PDF;

    public static final String DEFAULT_SHEET_NAME = "Sheet1";

    /**
     * 检查文档文件类型
     *
     * @param type 文件类型
     * @return 是否有效
     */
    public static boolean checkFileType(String type) {
        List<String> docFileExtensions = new ArrayList<>();
        docFileExtensions.add(EXCEL_XLS);
        docFileExtensions.add(EXCEL_XLSX);
        docFileExtensions.add(WORD_DOC);
        docFileExtensions.add(WORD_DOCX);
        docFileExtensions.add(PPT_PPT);
        docFileExtensions.add(PPT_PPTX);
        docFileExtensions.add(PDF_PDF);
        if (StringUtils.isBlank(type)) {
            return false;
        }
        return docFileExtensions.contains(type.toLowerCase());
    }

}
