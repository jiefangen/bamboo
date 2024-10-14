package org.panda.business.example.test.concurrent;

import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.tech.core.concurrent.async.DefaultProgressTask;
import org.panda.tech.core.concurrent.async.TaskProgress;

public class ExampleProgressTask extends DefaultProgressTask<TaskProgress<String>> {

    public ExampleProgressTask(TaskProgress<String> progress) {
        super(progress);
    }

    @Override
    protected void execute(TaskProgress<String> progress) {
        LogUtil.info(getClass(), "Progress task run params {}", JsonUtil.toJson(progress));
    }
}
