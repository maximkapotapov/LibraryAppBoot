package com.github.library_app_boot.controllers;

import com.github.library_app_boot.models.Member;
import com.github.library_app_boot.security.MemberDetails;
import com.github.library_app_boot.services.MemberService;
import com.github.library_app_boot.util.MemberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AuthController {

    private final MemberValidator memberValidator;
    private final MemberService memberService;

    @Autowired
    public AuthController(MemberValidator memberValidator, MemberService memberService) {
        this.memberValidator = memberValidator;
        this.memberService = memberService;
    }

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

    @GetMapping("/reg")
    public String regPage(@ModelAttribute("member") Member member) {
        return "auth/reg";
    }

    @PostMapping("/reg")
    public String regForm(@ModelAttribute("member") @Valid Member member,
                          BindingResult bindingResult) {
        memberValidator.validate(member, bindingResult);
        if(bindingResult.hasErrors())
            return "auth/reg";
        
        memberService.saveUser(member);
        return "redirect:/login";
    }
}
