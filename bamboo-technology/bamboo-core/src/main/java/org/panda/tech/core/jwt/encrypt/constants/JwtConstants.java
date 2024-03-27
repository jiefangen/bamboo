package org.panda.tech.core.jwt.encrypt.constants;

/**
 * JWT常量类
 */
public class JwtConstants {

    private JwtConstants() {
    }

    /**
     * JWT串前缀
     */
    public static final String JWT_PREFIX = "jwt:";

    /**
     * 非对称算法名称
     */
    public static final String ASYMMETRIC_ALGORITHM_NAME = "RSA";

    /**
     * 非对称算法规格
     */
    public static final int ASYMMETRIC_ALGORITHM_SIZE = 512;

}
