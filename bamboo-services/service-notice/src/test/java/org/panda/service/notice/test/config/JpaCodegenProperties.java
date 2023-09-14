package org.panda.service.notice.test.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class JpaCodegenProperties {

    @Value("${bamboo.dataJpa.codegen.entityLocation}")
    private String entityLocation;

    @Value("${bamboo.dataJpa.codegen.repoLocation}")
    private String repoLocation;

}
