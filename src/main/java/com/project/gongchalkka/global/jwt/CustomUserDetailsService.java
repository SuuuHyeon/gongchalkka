package com.project.gongchalkka.global.jwt;

import com.project.gongchalkka.global.exception.EntityNotFoundErrorException;
import com.project.gongchalkka.global.exception.ErrorCode;
import com.project.gongchalkka.member.entity.Member;
import com.project.gongchalkka.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username).orElseThrow(
                () -> new EntityNotFoundErrorException(ErrorCode.USER_NOT_FOUND)
        );
        // CustomUserDetails로 반환
        return new CustomUserDetails(member);
    }
}
