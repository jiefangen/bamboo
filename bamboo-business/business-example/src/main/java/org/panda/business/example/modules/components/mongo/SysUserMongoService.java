package org.panda.business.example.modules.components.mongo;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.business.example.modules.system.model.dto.SysUserDto;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.data.model.query.spec.Querying;
import org.panda.tech.data.mongo.support.MongoUnityRepoxSupport;
import org.panda.tech.data.mongo.util.MongoQueryUtil;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysUserMongoService extends MongoUnityRepoxSupport<SysUserDto, String> {

    @Resource
    private SysUserMongoRepox sysUserMongoRepox;

    public void save(SysUserDto sysUserDto) {
        // 查询库中是否存在
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(Criteria.where("user").is(sysUserDto.getUser()));
        Query query = MongoQueryUtil.buildAndQuery(criteriaList);
        long counts = getAccessTemplate().count(getEntityClass(), query);
        if (counts < 1) { // 只允许保存一个用户信息
            sysUserMongoRepox.save(sysUserDto);
        }
    }

    public QueryResult<SysUserDto> query(String keyword) {
        List<Criteria> criteriaList = new ArrayList<>();
        if (StringUtils.isNotEmpty(keyword)) {
            criteriaList.add(Criteria.where("user.username").is(keyword));
        }
//        criteriaList.add(Criteria.where("roleCodes").size(1));
        Querying querying = new Querying(3, 1);
        return super.query(criteriaList, querying);
    }

    public SysUserDto first() {
        List<Criteria> criteriaList = new ArrayList<>();
        Query query = MongoQueryUtil.buildAndQuery(criteriaList);
        return getAccessTemplate().first(getEntityClass(), query);
    }

    public long delete(String keyword) {
        Query query = new Query();
        if (StringUtil.isInteger(keyword)) {
            query.addCriteria(Criteria.where("userId").is(Integer.parseInt(keyword)));
        } else {
            query.addCriteria(Criteria.where("user.username").is(keyword));
        }
        return getAccessTemplate().delete(getEntityClass(), query);
    }
}
