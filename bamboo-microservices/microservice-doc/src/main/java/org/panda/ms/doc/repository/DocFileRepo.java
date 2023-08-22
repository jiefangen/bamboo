package org.panda.ms.doc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.panda.ms.doc.model.entity.DocFile;

/**
 * @author bamboo-code-generator
 */
public interface DocFileRepo extends JpaRepository<DocFile, Long> {
}
