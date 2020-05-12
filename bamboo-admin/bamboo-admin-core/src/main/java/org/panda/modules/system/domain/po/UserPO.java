package org.panda.modules.system.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

/**
 * 用户表java映射对象
 *
 * @author jvfagan
 * @since JDK 1.8  2020/5/5
 **/
@Setter
@Getter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private BigInteger id;

    private String username;

    private String password;

    private String phone;

    private String nickname;

    private String email;

    private String sex;

    private Integer disabled;

    private Date createTime;

    private Date updateTime;
}
