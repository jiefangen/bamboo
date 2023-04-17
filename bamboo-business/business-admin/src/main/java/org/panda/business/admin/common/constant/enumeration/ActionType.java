package org.panda.business.admin.common.constant.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

/**
 * 操作日志类型
 *
 * @author fangen
 * @since JDK 11 2022/5/6
 */
@Getter
@AllArgsConstructor
public enum ActionType {
    OTHER(0, "其它"),
    ADD(1, "新增"),
    DEL(2, "删除"),
    QUERY(3, "查询"),
    UPDATE(4, "修改"),
    LOGIN(5, "登录"),
    QUIT(6, "登出"),
    AUTH(7, "授权"),
    EXPORT(8, "导出"),
    IMPORT(9, "导入"),
    EMPTY(10, "清空");

    private int value;
    private String desc;

    public static Optional<String> getActionDesc(int value) {
        String desc = null;
        switch(value) {
            case 0:
                desc = OTHER.getDesc();
                break;
            case 1:
                desc = ADD.getDesc();
                break;
            case 2:
                desc = DEL.getDesc();
                break;
            case 3:
                desc = QUERY.getDesc();
                break;
            case 4:
                desc = UPDATE.getDesc();
                break;
            case 5:
                desc = LOGIN.getDesc();
                break;
            case 6:
                desc = QUIT.getDesc();
                break;
            case 7:
                desc = AUTH.getDesc();
                break;
            case 8:
                desc = EXPORT.getDesc();
                break;
            case 9:
                desc = IMPORT.getDesc();
                break;
            case 10:
                desc = EMPTY.getDesc();
                break;
            default:
                // do nothing
                break;
        }
        return Optional.of(desc);
    }
}
