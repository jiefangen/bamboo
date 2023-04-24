package org.panda.service.doc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.panda.service.doc.model.entity.DocFile;

/**
 * @author bamboo-code-generator
 */
public interface DocFileRepo extends JpaRepository<DocFile, Long> {
}
