package org.panda.service.doc.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.panda.tech.data.model.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "doc_excel")
public class DocExcel extends BaseEntity {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文档文件ID
     */
    @Column(name = "doc_id")
    private Integer docId;

    /**
     * 工作表名称
     */
    @Column(name = "sheet_name")
    private String sheetName;

    /**
     * 单元格行索引
     */
    @Column(name = "row_index")
    private Integer rowIndex;

    /**
     * 单元格列索引
     */
    @Column(name = "column_index")
    private Integer columnIndex;

    /**
     * 单元格值
     */
    @Column(name = "cell_value")
    private String cellValue;

}