package org.panda.business.official.test.concurrent;

import org.junit.jupiter.api.Test;
import org.panda.business.official.test.OfficialApplicationTest;
import org.panda.tech.core.concurrent.async.TaskProgress;
import org.springframework.beans.factory.annotation.Autowired;

public class ProgressTaskExecutorTest extends OfficialApplicationTest {

    @Autowired
    private OfficialProgressTaskExecutor progressTaskExecutor;

    @Test
    void progressTask() {
        for (int i = 0; i < 10; i++) {
            TaskProgress<String> progress = new TaskProgress<>("taskId:" + i);
            OfficialProgressTask task = new OfficialProgressTask(progress);
            progressTaskExecutor.submit(task);
        }
    }

}
