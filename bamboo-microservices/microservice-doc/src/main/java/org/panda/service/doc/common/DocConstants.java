package org.panda.service.doc.common;

import org.panda.bamboo.common.constant.FileExtensions;

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

    public static final String EXCEL_XLS = FileExtensions.XLS;
    public static final String EXCEL_XLSX = FileExtensions.XLSX;
    public static final String WORD_DOC = FileExtensions.DOC;
    public static final String WORD_DOCX = FileExtensions.DOCX;
    public static final String PPT_PPT = FileExtensions.PPT;
    public static final String PPT_PPTX = FileExtensions.PPTX;

    public static final String DEFAULT_SHEET_NAME = "Sheet1";

}
