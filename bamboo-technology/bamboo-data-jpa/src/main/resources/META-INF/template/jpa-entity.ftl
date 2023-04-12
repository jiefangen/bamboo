package ${packageName};

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import org.panda.data.model.entity.BaseEntity;

@Entity
@Getter
@Setter
public class ${className} extends BaseEntity {

@Id
@GeneratedValue
private Long id;

<#list fields as field>
    private ${types[field_index]} ${field};
</#list>

}