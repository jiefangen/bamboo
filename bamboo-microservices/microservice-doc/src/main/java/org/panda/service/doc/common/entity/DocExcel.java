package org.panda.service.doc.common.entity;

import lombok.Getter;
import lombok.Setter;
import org.panda.tech.data.model.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "doc_excel")
public class DocExcel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private java.lang.Long id;

    @Column(name = "doc_id")
    private java.lang.Integer docId;

    @Column(name = "sheet_name")
    private java.lang.String sheetName;

    @Column(name = "row_index")
    private java.lang.Integer rowIndex;

    @Column(name = "column_index")
    private java.lang.Integer columnIndex;

    @Column(name = "cell_value")
    private java.lang.String cellValue;

}