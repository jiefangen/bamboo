package org.panda.business.admin.v1.application.event;

import org.panda.tech.security.web.AuthoritiesBizExecutor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 监听事件，应用程序启动完成后执行初始化任务
 *
 * @author fangen
 **/
@Component
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // 权限集扩展业务执行
        AuthoritiesBizExecutor authoritiesBizExecutor = event.getApplicationContext().getBean(AuthoritiesBizExecutor.class);
        authoritiesBizExecutor.execute();
    }
}
