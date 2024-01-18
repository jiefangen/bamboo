package org.panda.tech.core.webmvc.jwt;

/**
 * JWT解析器
 */
public interface JwtParser {

    boolean isAvailable();

    <T> T parse(String type, String jwt, Class<T> expectedType);

}
