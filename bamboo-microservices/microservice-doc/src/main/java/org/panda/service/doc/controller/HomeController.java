package org.panda.service.doc.controller;

import org.panda.bamboo.core.context.ApplicationContextBean;
import org.panda.core.spec.restful.RestfulResult;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
@RequestMapping(value = "/home")
public class HomeController {

    @GetMapping
    public RestfulResult<String> home() {
        return RestfulResult.success("The doc microservice");
    }

    @GetMapping("/generate")
    public RestfulResult<String> generate() throws SQLException {
        DataSource dataSource = ApplicationContextBean.getBean(DataSource.class);
        Connection connection = DataSourceUtils.getConnection(dataSource);
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet rs = metaData.getTables(null, null, "doc_file", new String[]{ "TABLE" });
        if (!rs.next()) {
            return (RestfulResult<String>) RestfulResult.failure();
        }
        return RestfulResult.success();
    }

}
