package org.panda.service.doc.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.service.doc.model.entity.DocFile;
import org.panda.service.doc.model.param.DocFileQueryParam;
import org.panda.service.doc.repository.DocFileRepo;
import org.panda.service.doc.repository.DocFileRepox;
import org.panda.service.doc.service.DocFileService;
import org.panda.tech.data.jpa.util.QueryPageHelper;
import org.panda.tech.data.model.query.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocFileServiceImpl implements DocFileService {

    @Autowired
    private DocFileRepo docFileRepo;
    @Autowired
    private DocFileRepox docFileRepox;

    @Override
    public QueryResult<DocFile> getDocFileByPage(DocFileQueryParam queryParam) {
        Pageable pageable = PageRequest.of(queryParam.getPageNo(), queryParam.getPageSize(),
                Sort.by("createTime").descending());

        // 构建查询条件
        Specification<DocFile> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(queryParam.getKeyword())) {
                predicateList.add(criteriaBuilder.like(root.get("filename").as(String.class),
                        Strings.PERCENT + queryParam.getKeyword() + Strings.PERCENT));
            }
            return query.where(predicateList.toArray(new Predicate[predicateList.size()])).getRestriction();
        };
        Page<DocFile> docFilePage = docFileRepo.findAll(spec, pageable);
        QueryResult<DocFile> queryResult = QueryPageHelper.convertQueryResult(docFilePage);
        return queryResult;
    }

    @Override
    public QueryResult<DocFile> getDocument(DocFileQueryParam queryParam) {
        String entityName = docFileRepox.getEntityName();
        StringBuilder ql = new StringBuilder("from ").append(entityName).append(" t where t.")
                .append("filename").append("=:filename");
        Map<String, Object> params = new HashMap<>();
        params.put("filename", queryParam.getKeyword());
        return docFileRepox.query(ql, params, queryParam);
    }
}
