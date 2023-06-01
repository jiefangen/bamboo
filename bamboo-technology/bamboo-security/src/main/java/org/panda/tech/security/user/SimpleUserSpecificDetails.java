package org.panda.tech.security.user;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.panda.bamboo.common.util.clazz.ClassUtil;
import org.panda.tech.core.spec.user.UserIdentity;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 简单的用户特性细节
 *
 * @param <I> 用户标识类型
 */
public class SimpleUserSpecificDetails<I extends UserIdentity<?>> implements UserSpecificDetails<I> {

    private static final long serialVersionUID = 3697183508044079460L;

    private I identity;
    private String username;
    private String caption;
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    private Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
    private boolean enabled;
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;

    @Override
    public I getIdentity() {
        return this.identity;
    }

    public void setIdentity(I identity) {
        this.identity = identity;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getCaption() {
        return this.caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Override
    @SuppressWarnings("unchecked")
    public SimpleUserSpecificDetails<I> clone() {
        SimpleUserSpecificDetails<I> details = ClassUtil.newInstance(getClass());
        BeanUtils.copyProperties(this, details);
        return details;
    }

}
