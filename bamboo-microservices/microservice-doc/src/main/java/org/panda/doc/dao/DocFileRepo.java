package org.panda.doc.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.panda.doc.common.entity.DocFile;

/**
 * @author bamboo-code-generator
 */
public interface DocFileRepo extends JpaRepository<DocFile, Long> {
}
