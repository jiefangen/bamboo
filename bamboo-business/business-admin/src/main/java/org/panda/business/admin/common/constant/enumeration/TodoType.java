package org.panda.business.admin.common.constant.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作日志类型
 *
 * @author fangen
 * @since JDK 11 2022/5/6
 */
@Getter
@AllArgsConstructor
public enum TodoType {
    TODO("", 1),
    WORKING("", 2),
    DONE("", 3),
    DISCARD("", 4);

    private String key;
    private int value;
}
