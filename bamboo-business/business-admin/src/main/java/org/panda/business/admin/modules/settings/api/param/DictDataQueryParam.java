package org.panda.business.admin.modules.settings.api.param;

import lombok.Getter;
import lombok.Setter;
import org.panda.tech.data.model.query.QueryParam;

/**
 * 系统字典数据分页查询参数
 *
 * @author fangen
 **/
@Setter
@Getter
public class DictDataQueryParam extends QueryParam {
    private static final long serialVersionUID = -8421654406241390747L;

    private Integer dictId;
    /**
     * 字典键
     */
    private String dictKey;
    /**
     * 字典数据标签
     */
    private String dictLabel;
    /**
     * 状态
     */
    private Integer status;
}
