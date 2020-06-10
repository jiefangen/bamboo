package org.panda.core.modules.system.domain.po;

import lombok.Getter;
import lombok.Setter;
import org.panda.common.util.DateUtil;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

/**
 * 用户表java映射对象
 *
 * @author jiefangen
 * @since JDK 1.8  2020/5/5
 **/
@Setter
@Getter
public class UserPO implements Serializable{
    private static final long serialVersionUID = 2345642021650652148L;

    private BigInteger id;

    private String username;

    private transient String password;

    // shiro盐,用于加密验证
    private transient String salt;

    private String phone;

    private String nickname;

    private String email;

    private String sex;

    private Integer disabled;

    private Date createTime;

    private Date updateTime;

    private String createTimeStr;

    private String updateTimeStr;

    public String getCreateTimeStr() {
        return DateUtil.formatLong(this.createTime);
    }

    public String getUpdateTimeStr() {
        return DateUtil.formatLong(this.updateTime);
    }
}
