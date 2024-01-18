package org.panda.tech.core.webmvc.jwt;

/**
 * JWT生成器
 */
public interface JwtGenerator {

    boolean isAvailable();

    String generate(String type, Object source);

}
