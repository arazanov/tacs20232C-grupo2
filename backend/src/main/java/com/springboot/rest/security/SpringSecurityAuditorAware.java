package com.springboot.rest.security;

import com.springboot.rest.model.User;
import com.springboot.rest.security.services.CustomUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

class SpringSecurityAuditorAware implements AuditorAware<User> {

    @Override
    @NonNull
    public Optional<User> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(CustomUserDetails.class::cast)
                .map(CustomUserDetails::getUser);
    }

}
