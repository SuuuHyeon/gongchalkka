package com.project.gongchalkka.member.service;

import com.project.gongchalkka.global.exception.BusinessErrorException;
import com.project.gongchalkka.global.exception.EntityNotFoundErrorException;
import com.project.gongchalkka.global.exception.ErrorCode;
import com.project.gongchalkka.global.jwt.JwtTokenProvider;
import com.project.gongchalkka.member.dto.MemberLoginRequest;
import com.project.gongchalkka.member.dto.MemberSignupRequest;
import com.project.gongchalkka.member.dto.TokenReissueRequest;
import com.project.gongchalkka.member.dto.TokenResponse;
import com.project.gongchalkka.member.entity.Member;
import com.project.gongchalkka.member.entity.RefreshToken;
import com.project.gongchalkka.member.repository.MemberRepository;
import com.project.gongchalkka.member.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;        // MemberRepository 주입
    private final PasswordEncoder passwordEncoder;          // PasswordEncoder 주입
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;


    /// 회원가입
    @Transactional
    public Long signup(MemberSignupRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            //  Email 중복
            throw new BusinessErrorException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 엔티티 noargsconstructor 어노테이션 추가로 인한 수정사항
        Member member = new Member(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getNickname()
        );

        Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }


    @Transactional
    /// 로그인
    public TokenResponse login(MemberLoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(
                // 유저를 찾을 수 없음
                () -> new EntityNotFoundErrorException(ErrorCode.USER_NOT_FOUND)
        );

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BusinessErrorException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtTokenProvider.createAccessToken(member.getEmail(), member.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        refreshTokenRepository.findByMemberId(member.getId())
                .ifPresentOrElse(
                        // 이미 토큰이 존재하면 값 교체
                        (token) -> token.updateTokenValue(refreshToken),
                        // 없다면 새로 생성해서 저장
                        () -> refreshTokenRepository.save(new RefreshToken(member, refreshToken))
                );


        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    /// 토큰 재발급 메서드
    @Transactional
    public TokenResponse reissue(TokenReissueRequest request) {

        String refreshTokenValue = request.getRefreshToken();
        if (!jwtTokenProvider.validateToken(refreshTokenValue)) {
            throw new BusinessErrorException(ErrorCode.TOKEN_INVALID);
        }

        RefreshToken refreshToken = refreshTokenRepository.findByTokenValue(request.getRefreshToken()).orElseThrow(
                () -> new EntityNotFoundErrorException(ErrorCode.REFRESH_TOKEN_NOT_FOUND)
        );

        // 유저 정보
        Member member = refreshToken.getMember();

        String newAccessToken = jwtTokenProvider.createAccessToken(member.getEmail(), member.getId());
        // 실무에서는 보안을 위해 새 것으로 교체한다고 함
        // Refresh Token Rotation(RTR) 전략
        String newRefreshToken = jwtTokenProvider.createRefreshToken();

        refreshToken.updateTokenValue(newRefreshToken);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }


    /**
     * 로그아웃 메서드
     */
    @Transactional
    public void logout(Principal principal) {
        String email = principal.getName(); // 사용자

        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundErrorException(ErrorCode.USER_NOT_FOUND)
        );

        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(member.getId()).orElseThrow(
                () -> new EntityNotFoundErrorException(ErrorCode.REFRESH_TOKEN_NOT_FOUND)
        );

        refreshTokenRepository.delete(refreshToken);
    }

    /**
     * 멤버 검증 메서드
     */
    public Member validateMember(Principal principal) {
        // 유저 정보 검증
        return memberRepository.findByEmail(principal.getName()).orElseThrow(
                () -> new EntityNotFoundErrorException(ErrorCode.USER_NOT_FOUND)
        );
    }

}
