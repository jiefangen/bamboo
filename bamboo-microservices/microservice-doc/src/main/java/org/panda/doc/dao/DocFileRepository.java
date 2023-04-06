package org.panda.doc.dao;

import org.panda.doc.common.entity.DocFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocFileRepository extends JpaRepository<DocFile, Long> {

}
