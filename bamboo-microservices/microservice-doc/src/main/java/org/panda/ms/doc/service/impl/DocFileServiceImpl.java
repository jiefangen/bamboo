package org.panda.ms.doc.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.ms.doc.model.entity.DocFile;
import org.panda.ms.doc.model.param.DocFileQueryParam;
import org.panda.ms.doc.repository.DocFileRepo;
import org.panda.ms.doc.repository.DocFileRepox;
import org.panda.ms.doc.service.DocFileService;
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
import java.util.List;

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
}
