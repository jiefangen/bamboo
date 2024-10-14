package org.panda.business.example.modules.components.mongo;

import io.swagger.annotations.Api;
import org.panda.business.example.modules.system.model.dto.SysUserDto;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.data.model.query.QueryResult;
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
    public RestfulResult<?> query(@RequestParam(required = false) String keyword) {
        QueryResult<SysUserDto> result = sysUserMongoService.query(keyword);
        return RestfulResult.success(result);
    }

    @GetMapping("/first")
    public RestfulResult<?> first() {
        SysUserDto result = sysUserMongoService.first();
        return RestfulResult.success(result);
    }

    @GetMapping("/delete")
    public RestfulResult<?> delete(@RequestParam String keyword) {
        long result = sysUserMongoService.delete(keyword);
        return RestfulResult.success(result);
    }

}
