package org.panda.service.auth.common.constant;

/**
 * 资源权限限定常量
 *
 * @author fangen
 **/
public class Authorization {
    /**
     * 账户类型
     */
    public static final String TYPE_MANAGER = "manager";
    public static final String TYPE_SYSTEM = "system";
    public static final String TYPE_GENERAL = "general";
    public static final String TYPE_CUSTOMER = "customer";

    /**
     * 账户等级
     */
    public static final String RANK_0 = "0";
    public static final String RANK_1 = "1";
    public static final String RANK_2 = "2";
    public static final String RANK_3 = "3";

    /**
     * 角色权限
     */
    public static final String PER_ADMIN = "ADMIN";
    public static final String PER_SYSTEM = "SYSTEM";
    public static final String PER_ACTUATOR = "ACTUATOR";
    public static final String PER_USER = "USER";
    public static final String PER_GENERAL = "GENERAL";
    public static final String PER_CUSTOMER = "CUSTOMER";

}
