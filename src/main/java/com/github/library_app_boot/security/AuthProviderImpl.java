package com.github.library_app_boot.security;

import com.github.library_app_boot.services.MemberDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AuthProviderImpl implements AuthenticationProvider {
    private final MemberDetailsService memberDetailsService;

    @Autowired
    public AuthProviderImpl(MemberDetailsService memberDetailsService) {
        this.memberDetailsService = memberDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();

        UserDetails memberDetails = memberDetailsService.loadUserByUsername(username);

        String password = authentication.getCredentials().toString();

        if(!password.equals(memberDetails.getPassword()))
            throw new BadCredentialsException("Wrong password!");

        return new UsernamePasswordAuthenticationToken(memberDetails, password, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
