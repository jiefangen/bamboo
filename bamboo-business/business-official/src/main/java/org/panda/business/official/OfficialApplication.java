package org.panda.business.official;

import org.panda.tech.core.CoreModule;
import org.panda.tech.security.SecurityModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({CoreModule.class, SecurityModule.class})
public class OfficialApplication {
    public static void main(String[] args) {
        SpringApplication.run(OfficialApplication.class,args);
    }
}
