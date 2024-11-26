package org.panda.service.doc.test.common;

import org.junit.jupiter.api.Test;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.date.TemporalUtil;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CommonTest {

    @Test
    void dateFormatTest() {
        String nowLocalDateTime = TemporalUtil.format(LocalDateTime.now());
        LogUtil.info(getClass(), nowLocalDateTime);
        String nowLocalDate = TemporalUtil.format(LocalDate.now());
        LogUtil.info(getClass(), nowLocalDate);
        String longNoDelimiter = TemporalUtil.formatLongNoDelimiter(Instant.now());
        LogUtil.info(getClass(), longNoDelimiter);
    }
}
