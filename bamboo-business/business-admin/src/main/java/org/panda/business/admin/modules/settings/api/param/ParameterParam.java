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
    private String parameterName;

    @NotBlank
    private String parameterKey;

    @NotBlank
    private String parameterValue;

    private String parameterType;

    private Integer status;

    private String appRange;

    private String remark;

    public void transform(SysParameter parameter) {
        parameter.setId(this.getId());
        parameter.setParameterName(this.getParameterName());
        parameter.setParameterKey(this.getParameterKey());
        parameter.setParameterValue(this.getParameterValue());
        parameter.setParameterType(this.getParameterType());
        parameter.setStatus(this.getStatus());
        parameter.setAppRange(this.getAppRange());
        parameter.setRemark(this.getRemark());
    }
}
