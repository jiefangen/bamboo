package org.panda.doc.dao;

import org.panda.data.jpa.support.JpaRepoxSupport;
import org.panda.doc.common.entity.DocFile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DocFileRepox extends JpaRepoxSupport<DocFile> {

    public List<DocFile> findAll() {
         String entityName = getEntityName();
         StringBuilder hql = new StringBuilder(" from ").append(entityName)
                .append(" where 1=1 ");
        return getAccessTemplate().list(hql);
    }

}
