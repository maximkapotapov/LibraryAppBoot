package com.github.library_app_boot.controllers;

import com.github.library_app_boot.dto.MemberDTO;
import com.github.library_app_boot.models.Member;
import com.github.library_app_boot.security.JWTUtil;
import com.github.library_app_boot.security.MemberDetails;
import com.github.library_app_boot.services.MemberService;
import com.github.library_app_boot.util.MemberErrorResponse;
import com.github.library_app_boot.util.MemberNotCreatedException;
import com.github.library_app_boot.util.MemberValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AuthController {

    private final MemberValidator memberValidator;
    private final MemberService memberService;

    private final ModelMapper modelMapper;

    private final JWTUtil jwtUtil;

    @Autowired
    public AuthController(MemberValidator memberValidator, MemberService memberService, ModelMapper modelMapper, JWTUtil jwtUtil) {
        this.memberValidator = memberValidator;
        this.memberService = memberService;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
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
    public Map<String, String> regForm(@RequestBody @Valid MemberDTO memberDTO,
                                       BindingResult bindingResult) {
        Member member = convertToMember(memberDTO);

        memberValidator.validate(member, bindingResult);
        if(bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error: errors) {
                errorMessage.append(error.getField()).append("-")
                        .append(error.getDefaultMessage()).append(";");
            }
            throw new MemberNotCreatedException(errorMessage.toString());
        }

        memberService.saveUser(member);

        String token = jwtUtil.generateToken(member.getUsername());

        return Map.of("jwt-token", token);
    }

    private Member convertToMember(MemberDTO memberDTO) {
        return modelMapper.map(memberDTO, Member.class);
    }

    @ExceptionHandler
    private ResponseEntity<MemberErrorResponse> handleException (MemberNotCreatedException e) {
        MemberErrorResponse response = new MemberErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
