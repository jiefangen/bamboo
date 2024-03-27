package org.panda.tech.core.jwt.encrypt;

/**
 * JWT生成器
 */
public interface JwtGenerator {

    boolean isAvailable();

    String generate(String type, Object source);

}
