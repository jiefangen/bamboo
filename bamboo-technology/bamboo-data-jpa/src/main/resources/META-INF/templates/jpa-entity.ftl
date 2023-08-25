package ${packageName};

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.panda.tech.data.model.entity.BaseEntity;

import javax.persistence.*;
<#list importJavaTypes as importJavaType>
import ${importJavaType};
</#list>

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "${tableName}")
public class ${entityName} extends BaseEntity {

    private static final long serialVersionUID = 1L;

<#list fieldMetaDataList as fieldMetaData>
    <#if fieldMetaData.remarks?? && fieldMetaData.remarks?length != 0>
    /**
     * ${fieldMetaData.remarks}
     */
    </#if>
    <#if fieldMetaData_index = 0>
    @Id
        <#if fieldMetaData.autoIncrement>
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        <#else>
    @GeneratedValue
        </#if>
    <#else>
    @Column(name = "${fieldMetaData.columnName}")
    </#if>
    private ${fieldMetaData.fieldTypeName} ${fieldMetaData.fieldName};

</#list>
}