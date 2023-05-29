package org.panda.tech.security.user;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.panda.tech.core.spec.user.UserIdentity;
import org.panda.tech.core.spec.user.UserSpecific;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户特性细节
 *
 * @param <I> 用户标识类型
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public interface UserSpecificDetails<I extends UserIdentity<?>> extends UserSpecific<I>, UserDetails {

    I getIdentity();

    String getCaption();

    UserSpecificDetails<I> clone();

}
