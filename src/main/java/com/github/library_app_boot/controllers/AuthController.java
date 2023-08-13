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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class AuthController {

    private final MemberValidator memberValidator;
    private final MemberService memberService;

    private final ModelMapper modelMapper;

    private final JWTUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(MemberValidator memberValidator, MemberService memberService, ModelMapper modelMapper, JWTUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.memberValidator = memberValidator;
        this.memberService = memberService;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/show-user-info")
    public String showMemberInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        return memberDetails.getUsername();
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

    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody MemberDTO memberDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(memberDTO.getUsername(), memberDTO.getPassword());

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            return Map.of("Error!", "Wrong Credentials!");
        }

        String token = jwtUtil.generateToken(memberDTO.getUsername());

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
