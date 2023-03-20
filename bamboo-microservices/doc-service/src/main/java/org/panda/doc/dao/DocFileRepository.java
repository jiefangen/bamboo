package org.panda.doc.dao;

import org.panda.doc.model.entity.DocFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocFileRepository extends JpaRepository<DocFile, Long> {

}
