package org.panda.business.admin.v1.application.event;

import org.panda.business.admin.v1.modules.system.service.SysUserService;
import org.panda.tech.security.user.UserConfigAuthority;
import org.panda.tech.security.web.AuthoritiesBizExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * 监听事件，应用程序启动完成后执行初始化任务
 *
 * @author fangen
 **/
@Component
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private AuthoritiesBizExecutor authoritiesBizExecutor;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Map<String, Collection<UserConfigAuthority>> apiConfigAuthoritiesMapping = authoritiesBizExecutor.getApiConfigAuthoritiesMapping();
        SysUserService userService = event.getApplicationContext().getBean(SysUserService.class);

        System.out.println();
    }
}
