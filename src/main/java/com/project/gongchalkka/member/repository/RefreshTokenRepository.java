package com.project.gongchalkka.member.repository;

import com.project.gongchalkka.member.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    // (재발급 시) 토큰 값으로 토큰 객체 찾기
    Optional<RefreshToken> findByTokenValue(String tokenValue);
    // (로그인 시) 사용자 아이디로 토큰 객체 찾기
    Optional<RefreshToken> findByMemberId(Long memberId);
}
