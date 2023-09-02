package org.panda.business.admin.modules.monitor.api.controller;

import io.swagger.annotations.Api;
import org.panda.business.admin.common.constant.Authority;
import org.panda.business.admin.modules.monitor.api.param.OnlineQueryParam;
import org.panda.business.admin.modules.monitor.api.vo.OnlineVO;
import org.panda.business.admin.modules.monitor.service.SysUserTokenService;
import org.panda.tech.core.spec.log.ActionType;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.config.annotation.WebOperationLog;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.security.config.annotation.ConfigAuthorities;
import org.panda.tech.security.config.annotation.ConfigAuthority;
import org.panda.tech.security.config.annotation.ConfigPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 在线用户
 *
 * @author fangen
 */
@Api(tags = "系统在线用户")
@RestController
@RequestMapping("/monitor/online")
public class OnlineController {

    @Autowired
    private SysUserTokenService userTokenService;

    @PostMapping("/page")
    @ConfigPermission
    public RestfulResult online(@RequestBody OnlineQueryParam queryParam) {
        QueryResult<OnlineVO> actionLogPage = userTokenService.getOnlineByPage(queryParam);
        return RestfulResult.success(actionLogPage);
    }

    @PutMapping("/quit/{tokenId}")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_ACTUATOR),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "monitor_online_quit")
    })
    @WebOperationLog(actionType = ActionType.QUIT, intoStorage = true)
    public RestfulResult quit(HttpServletRequest request, @PathVariable Long tokenId){
        String token = request.getHeader(WebConstants.HEADER_AUTH_JWT);
        if (userTokenService.quitOnlineUser(tokenId, token)) {
            return RestfulResult.success();
        } else {
            return RestfulResult.failure();
        }
    }

}
