package org.panda.ms.doc.service.impl;

import org.panda.ms.doc.repository.DocFileRepo;
import org.panda.ms.doc.model.entity.DocFile;
import org.panda.ms.doc.service.DocFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DocFileServiceImpl implements DocFileService {

    @Autowired
    private DocFileRepo docFileRepo;

    @Override
    public void save(DocFile docFile) {
        docFile.setAccessibility(true);
        LocalDateTime currentTime = LocalDateTime.now();
        docFile.setCreateTime(currentTime);
        docFile.setUpdateTime(currentTime);
        docFileRepo.save(docFile);
    }
}
