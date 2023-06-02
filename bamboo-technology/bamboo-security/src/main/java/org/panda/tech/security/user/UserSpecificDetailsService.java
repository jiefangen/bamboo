package org.panda.tech.security.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserSpecificDetailsService extends UserDetailsService {
    void setPasswordEncoder(PasswordEncoder passwordEncoder);
}
