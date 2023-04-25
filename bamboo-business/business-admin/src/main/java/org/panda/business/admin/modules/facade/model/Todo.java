package org.panda.business.admin.modules.facade.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

/**
 * 待办事项列表
 *
 * @author fangen
 * @since JDK 11 2022/5/10
 */
@Setter
@Getter
public class Todo {
    private String id;

    private BigInteger userId;

    private Integer workStatus;

    private String content;

    private Integer sort;

    private Date createTime;

    private Date updateTime;

    // 冗余字段-是否可编辑
    boolean edit = false;
}
