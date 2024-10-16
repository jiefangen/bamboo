package org.panda.business.official.application.event;

import org.panda.bamboo.common.util.LogUtil;
import org.panda.tech.core.boot.ApplicationContextRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 监听事件，应用程序启动完成后执行初始化任务
 *
 * @author fangen
 **/
@Component
public class ApplicationReadyListener implements ApplicationContextRunner {

    @Override
    public void run(ApplicationContext context) throws Exception {
        LogUtil.info(getClass(), "Application startup message monitoring");
    }
}
