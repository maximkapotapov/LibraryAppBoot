package com.github.library_app_boot.controllers;

import com.github.library_app_boot.security.MemberDetails;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/show-user-info")
    public String showMemberInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        System.out.println(memberDetails.getMember());
        return "redirect:/people";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }
}
