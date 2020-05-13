package org.panda.modules.system.domain.po;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

/**
 * 角色表映射对象
 *
 * @author jvfagan
 * @since JDK 1.8  2020/5/5
 **/
@Setter
@Getter
public class RolePO implements Serializable{
    private static final long serialVersionUID = 1L;

    private BigInteger id;

    private String roleName;

    private String roleCode;

    private String description;

    private Date createTime;

    private Date updateTime;

}
