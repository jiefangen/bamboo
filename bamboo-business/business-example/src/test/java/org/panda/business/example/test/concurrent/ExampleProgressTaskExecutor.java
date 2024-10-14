package org.panda.business.example.test.concurrent;

import org.panda.tech.core.concurrent.ExecutorUtil;
import org.panda.tech.core.concurrent.async.ProgressTaskExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExampleProgressTaskExecutor extends ProgressTaskExecutor {
    @Autowired
    public ExampleProgressTaskExecutor() {
        super(ExecutorUtil.buildDefaultExecutor());
    }
}
