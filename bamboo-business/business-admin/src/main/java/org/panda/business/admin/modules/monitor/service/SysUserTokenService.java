package org.panda.business.admin.modules.monitor.service;

import org.panda.business.admin.modules.monitor.api.param.OnlineQueryParam;
import org.panda.business.admin.modules.monitor.api.vo.OnlineVO;
import org.panda.business.admin.modules.monitor.service.entity.SysUserToken;
import com.baomidou.mybatisplus.extension.service.IService;
import org.panda.tech.data.model.query.QueryResult;

/**
 * <p>
 * 系统用户token 服务类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-07-21
 */
public interface SysUserTokenService extends IService<SysUserToken> {

    QueryResult<OnlineVO> getOnlineByPage(OnlineQueryParam queryParam);

    boolean quitOnlineUser(Long tokenId, String token);

    void kickOutOnlineUsers(Integer userId, String username);

    void refreshTokenStatus();

    void cleanObsoleteToken();
}
