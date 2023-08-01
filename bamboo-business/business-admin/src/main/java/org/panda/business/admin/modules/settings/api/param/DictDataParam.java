package org.panda.business.admin.modules.settings.api.param;

import lombok.Getter;
import lombok.Setter;
import org.panda.business.admin.modules.settings.service.entity.SysDictionaryData;

import javax.validation.constraints.NotBlank;

/**
 * 字典数据参数
 *
 * @author fangen
 **/
@Setter
@Getter
public class DictDataParam {

    private Integer id;

    private Integer dictId;

    @NotBlank
    private String dictKey;

    @NotBlank
    private String dictLabel;

    @NotBlank
    private String dictValue;

    private Integer status;

    private String isDefault;

    private String remark;

    private Integer sort;

    private String echoClass;

    private String styleAttribute;

    public void transform(SysDictionaryData dictData) {
        dictData.setId(this.id);
        dictData.setDictId(this.dictId);
        dictData.setDictLabel(this.dictLabel);
        dictData.setDictValue(this.dictValue);
        dictData.setStatus(this.status);
        dictData.setIsDefault(this.isDefault);
        dictData.setRemark(this.remark);
        dictData.setSort(this.sort);
        dictData.setEchoClass(this.echoClass);
        dictData.setStyleAttribute(this.styleAttribute);
    }
}
