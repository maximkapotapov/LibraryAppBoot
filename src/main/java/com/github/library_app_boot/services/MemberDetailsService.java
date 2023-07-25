package com.github.library_app_boot.services;

import com.github.library_app_boot.models.Member;
import com.github.library_app_boot.repositories.MemberRepository;
import com.github.library_app_boot.security.MemberDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findByUsername(username);

        if(member.isEmpty())
            throw new UsernameNotFoundException("User is not found!");

        return new MemberDetails(member.get());
    }
}
