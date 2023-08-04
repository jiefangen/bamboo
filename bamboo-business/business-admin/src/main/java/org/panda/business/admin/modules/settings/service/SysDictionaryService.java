package org.panda.business.admin.modules.settings.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.business.admin.modules.settings.api.param.DictionaryParam;
import org.panda.business.admin.modules.settings.api.param.DictionaryQueryParam;
import org.panda.business.admin.modules.settings.service.entity.SysDictionary;
import org.panda.tech.data.model.query.QueryResult;

/**
 * <p>
 * 系统字典 服务类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-07-29
 */
public interface SysDictionaryService extends IService<SysDictionary> {

    QueryResult<SysDictionary> getDictByPage(DictionaryQueryParam queryParam);

    String addDictionary(DictionaryParam dictionaryParam);

    boolean updateDictionary(DictionaryParam dictionaryParam);

    boolean deleteDictionary(Integer id) throws BusinessException;
}
