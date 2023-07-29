package com.github.library_app_boot.util;

import com.github.library_app_boot.models.Member;
import com.github.library_app_boot.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MemberValidator  implements Validator {

    private final MemberService memberService;

    @Autowired
    public MemberValidator(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Member.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Member member = (Member) o;

        if (memberService.findUser(member.getUsername()).isPresent()) {
            errors.rejectValue("username", "", "That name is already taken");
        }

    }
}
