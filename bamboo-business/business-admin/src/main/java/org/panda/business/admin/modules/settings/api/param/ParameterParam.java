package org.panda.business.admin.modules.settings.api.param;

import lombok.Getter;
import lombok.Setter;
import org.panda.business.admin.modules.settings.service.entity.SysParameter;

import javax.validation.constraints.NotBlank;

/**
 * 参数设置
 *
 * @author fangen
 **/
@Setter
@Getter
public class ParameterParam {

    private Integer id;

    @NotBlank
    private String paramName;

    @NotBlank
    private String paramKey;

    @NotBlank
    private String paramValue;

    private String paramType;

    private Integer status;

    private String appRange;

    private String remark;

    public void transform(SysParameter parameter) {
        parameter.setId(this.getId());
        parameter.setParamName(this.getParamName());
        parameter.setParamKey(this.getParamKey());
        parameter.setParamValue(this.getParamValue());
        parameter.setParamType(this.getParamType());
        parameter.setStatus(this.getStatus());
        parameter.setAppRange(this.getAppRange());
        parameter.setRemark(this.getRemark());
    }
}
