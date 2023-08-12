package org.panda.service.doc.api;

import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/home")
public class HomeController {

    @GetMapping
    public RestfulResult<String> home() {
        return RestfulResult.success("The doc microservice");
    }

}
