package com.github.library_app_boot.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.library_app_boot.security.JWTUtil;
import com.github.library_app_boot.services.MemberDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final MemberDetailsService memberDetailsService;

    public JWTFilter(JWTUtil jwtUtil, MemberDetailsService memberDetailsService) {
        this.jwtUtil = jwtUtil;
        this.memberDetailsService = memberDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer")) {
            String jwt = authHeader.substring(7);

            if(jwt.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid JWT Token Bearer");
            } else {
                try {

                String username = jwtUtil.verifyToken(jwt);

                UserDetails userDetails = memberDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                        userDetails.getAuthorities());

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                } catch (JWTVerificationException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "Could not verify JWT Token");
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
