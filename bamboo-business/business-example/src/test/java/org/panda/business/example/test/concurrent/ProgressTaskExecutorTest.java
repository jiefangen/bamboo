package org.panda.business.example.test.concurrent;

import org.junit.jupiter.api.Test;
import org.panda.business.example.test.ExampleApplicationTest;
import org.panda.tech.core.concurrent.async.TaskProgress;
import org.springframework.beans.factory.annotation.Autowired;

public class ProgressTaskExecutorTest extends ExampleApplicationTest {

    @Autowired
    private ExampleProgressTaskExecutor progressTaskExecutor;

    @Test
    void progressTask() {
        for (int i = 0; i < 10; i++) {
            TaskProgress<String> progress = new TaskProgress<>("taskId:" + i);
            ExampleProgressTask task = new ExampleProgressTask(progress);
            progressTaskExecutor.submit(task);
        }
    }
}
