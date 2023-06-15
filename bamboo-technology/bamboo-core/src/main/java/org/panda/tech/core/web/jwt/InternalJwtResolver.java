package org.panda.tech.core.web.jwt;

/**
 * 内部JWT解决器
 */
public interface InternalJwtResolver {

    boolean isGenerable(String appName);

    String generate(String appName, Object source);

    boolean isParsable();

    <T> T parse(String jwt, Class<T> type);

    boolean verify(String jwt);

}
