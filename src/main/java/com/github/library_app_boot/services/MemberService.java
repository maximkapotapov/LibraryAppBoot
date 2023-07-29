package com.github.library_app_boot.services;

import com.github.library_app_boot.models.Member;
import com.github.library_app_boot.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    public Optional<Member> findUser(String username) {
        return memberRepository.findByUsername(username);
    }

    @Transactional
    public void saveUser(Member member) {
        memberRepository.save(member);
    }
}
