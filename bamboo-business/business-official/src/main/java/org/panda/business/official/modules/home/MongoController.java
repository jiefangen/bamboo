package org.panda.business.official.modules.home;

import io.swagger.annotations.Api;
import org.panda.business.official.modules.system.service.SysUserMongoService;
import org.panda.business.official.modules.system.service.dto.SysUserDto;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.security.config.annotation.ConfigAnonymous;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "NoSql数据库控制器")
@RestController
@RequestMapping("/mongo")
public class MongoController {

    @Autowired
    private SysUserMongoService sysUserMongoService;

    @GetMapping("/query")
    @ConfigAnonymous
    public RestfulResult<?> query(@RequestParam(required = false) String keyword) {
        QueryResult<SysUserDto> result = sysUserMongoService.query(keyword);
        return RestfulResult.success(result);
    }

    @GetMapping("/first")
    @ConfigAnonymous
    public RestfulResult<?> first() {
        SysUserDto result = sysUserMongoService.first();
        return RestfulResult.success(result);
    }

    @GetMapping("/delete")
    @ConfigAnonymous
    public RestfulResult<?> delete(@RequestParam String keyword) {
        long result = sysUserMongoService.delete(keyword);
        return RestfulResult.success(result);
    }

}
