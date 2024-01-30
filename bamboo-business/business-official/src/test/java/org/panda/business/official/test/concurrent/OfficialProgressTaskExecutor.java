package org.panda.business.official.test.concurrent;

import org.panda.tech.core.concurrent.ExecutorUtil;
import org.panda.tech.core.concurrent.async.ProgressTaskExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OfficialProgressTaskExecutor extends ProgressTaskExecutor {
    @Autowired
    public OfficialProgressTaskExecutor() {
        super(ExecutorUtil.buildDefaultExecutor());
    }
}
