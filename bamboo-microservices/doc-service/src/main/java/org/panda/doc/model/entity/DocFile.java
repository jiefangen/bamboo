package org.panda.doc.model.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DocFile {
    @Id
    private Long id;
}
