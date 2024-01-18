package org.panda.tech.core.web.annotation;

import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * 只读事务
 *
 * @author jianglei
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Transactional(readOnly = true)
public @interface ReadTransactional {
}
