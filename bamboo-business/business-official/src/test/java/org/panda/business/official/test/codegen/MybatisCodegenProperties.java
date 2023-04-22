package org.panda.business.official.test.codegen;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
//@Configuration
public class MybatisCodegenProperties {

    @Value("${bamboo.dataJpa.codegen.entityLocation}")
    private String entityLocation;

    @Value("${bamboo.dataJpa.codegen.repoLocation}")
    private String repoLocation;

}
