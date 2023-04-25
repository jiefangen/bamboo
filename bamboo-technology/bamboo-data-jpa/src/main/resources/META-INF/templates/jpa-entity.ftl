package ${packageName};

import lombok.Getter;
import lombok.Setter;
import org.panda.tech.data.model.entity.BaseEntity;

import javax.persistence.*;
<#list importJavaTypes as importJavaType>
import ${importJavaType};
</#list>

@Entity
@Getter
@Setter
@Table(name = "${tableName}")
public class ${entityName} extends BaseEntity {
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