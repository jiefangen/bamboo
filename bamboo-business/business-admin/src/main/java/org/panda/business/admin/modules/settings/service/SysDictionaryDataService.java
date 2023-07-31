package org.panda.business.admin.modules.settings.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.business.admin.modules.settings.api.param.DictDataParam;
import org.panda.business.admin.modules.settings.api.param.DictDataQueryParam;
import org.panda.business.admin.modules.settings.service.entity.SysDictionaryData;
import org.panda.tech.data.model.query.QueryResult;

/**
 * <p>
 * 系统字典数据 服务类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-07-29
 */
public interface SysDictionaryDataService extends IService<SysDictionaryData> {

    QueryResult<SysDictionaryData> getDictDataByPage(DictDataQueryParam queryParam);

    String addDictData(DictDataParam dictDataParam);

    boolean updateDictData(DictDataParam dictDataParam);

    boolean deleteDictData(Integer id) throws BusinessException;
}
