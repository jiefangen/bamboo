package org.panda.business.official.modules.system.service;

import org.apache.commons.lang3.StringUtils;
import org.panda.business.official.modules.system.service.dto.SysUserDto;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.data.model.query.spec.Querying;
import org.panda.tech.data.mongo.support.MongoUnityRepoxSupport;
import org.panda.tech.data.mongo.util.MongoQueryUtil;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysUserMongoService extends MongoUnityRepoxSupport<SysUserDto, String> {

    public QueryResult<SysUserDto> query(String keyword) {
        List<Criteria> criteriaList = new ArrayList<>();
        if (StringUtils.isNotEmpty(keyword)) {
//            criteriaList.add(Criteria.where("userId").is(Integer.valueOf(keyword)));
            criteriaList.add(Criteria.where("roles.roleName").is(keyword));
        }
        criteriaList.add(Criteria.where("roleCodes").size(1));
        Querying querying = new Querying(3, 1);
        return super.query(criteriaList, querying);
    }

    public SysUserDto first() {
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(new Criteria());
        Query query = MongoQueryUtil.buildQuery(criteriaList);
        SysUserDto sysUserDto = getAccessTemplate().first(getEntityClass(), query);
        return sysUserDto;
    }


    public long delete(String keyword) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(Integer.valueOf(keyword)));
        return getAccessTemplate().delete(getEntityClass(), query);
    }

}
