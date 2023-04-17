package org.panda.business.admin.modules.system.domain.po;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * 角色表映射对象
 *
 * @author jiefangen
 * @since JDK 1.8  2020/5/5
 **/
@Setter
@Getter
public class RolePO implements Serializable{
    private static final long serialVersionUID = 2345642021650676540L;

    private BigInteger id;

    private String roleName;

    private String roleCode;

    private String description;

    private Date createTime;

    private Date updateTime;
}
