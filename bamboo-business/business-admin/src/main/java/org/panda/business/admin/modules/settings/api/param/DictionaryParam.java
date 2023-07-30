package org.panda.business.admin.modules.settings.api.param;

import lombok.Getter;
import lombok.Setter;
import org.panda.business.admin.modules.settings.service.entity.SysDictionary;

import javax.validation.constraints.NotBlank;

/**
 * 字典参数
 *
 * @author fangen
 **/
@Setter
@Getter
public class DictionaryParam {

    private Integer id;

    @NotBlank
    private String dictName;

    @NotBlank
    private String dictKey;

    private String dictType;

    private Integer status;

    private String appRange;

    private String remark;

    public void transform(SysDictionary dictionary) {
        dictionary.setId(this.id);
        dictionary.setDictName(this.dictName);
        dictionary.setDictKey(this.dictKey);
        dictionary.setDictType(this.dictType);
        dictionary.setStatus(this.status);
        dictionary.setAppRange(this.appRange);
        dictionary.setRemark(this.remark);
    }
}
