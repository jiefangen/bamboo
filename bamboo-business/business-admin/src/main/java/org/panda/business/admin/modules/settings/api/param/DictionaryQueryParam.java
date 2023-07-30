package org.panda.business.admin.modules.settings.api.param;

import lombok.Getter;
import lombok.Setter;
import org.panda.tech.data.model.query.QueryParam;

/**
 * 系统字典分页查询参数
 *
 * @author jiefangen
 **/
@Setter
@Getter
public class DictionaryQueryParam extends QueryParam {
    private static final long serialVersionUID = 8039909336847637521L;

    /**
     * 字典名称
     */
    private String dictName;
    /**
     * 字典键名
     */
    private String dictKey;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 筛选开始时间
     */
    private String startDate;
    /**
     * 筛选结束时间
     */
    private String endDate;
}
