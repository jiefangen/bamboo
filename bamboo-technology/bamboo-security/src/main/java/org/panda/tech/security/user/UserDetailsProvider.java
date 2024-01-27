package org.panda.tech.security.user;

import org.panda.tech.core.spec.user.UserIdentity;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * 用户细节提供者
 */
public interface UserDetailsProvider {

    default <K extends Serializable> K getUserId() {
        UserIdentity<K> userIdentity = getUserIdentity();
        return userIdentity == null ? null : userIdentity.getId();
    }

    <I extends UserIdentity<?>> I getUserIdentity();

    <D extends UserSpecificDetails<?>> D getUserSpecificDetails();

    default void ifPresent(Consumer<UserSpecificDetails<?>> consumer) {
        UserSpecificDetails<?> userDetails = getUserSpecificDetails();
        if (userDetails != null) {
            consumer.accept(userDetails);
        }
    }

}
