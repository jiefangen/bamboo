package org.panda.modules.system.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限管理
 *
 * @author fangen
 * @since JDK 11 2022/4/16
 */
@Api(tags = "系统权限管理")
@RestController
@RequestMapping("/auth/system/permission")
public class PermissionController {
}
