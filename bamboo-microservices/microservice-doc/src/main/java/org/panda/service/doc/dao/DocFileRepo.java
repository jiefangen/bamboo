package org.panda.service.doc.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.panda.service.doc.common.entity.DocFile;

/**
 * @author bamboo-code-generator
 */
public interface DocFileRepo extends JpaRepository<DocFile, Long> {
}
