package org.panda.service.doc.model.param;

import lombok.Data;

/**
 * Excel文档文件上传参数
 *
 * @author fangen
 **/
@Data
public class ExcelDocFileParam extends DocFileParam {
    /**
     * Sheet页名称
     */
    private String sheetName;
    /**
     * 文件标签
     */
    private String tags;
}
