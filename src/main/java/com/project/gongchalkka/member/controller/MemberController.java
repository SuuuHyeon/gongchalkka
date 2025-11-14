package com.project.gongchalkka.member.controller;

import com.project.gongchalkka.member.dto.MemberLoginRequest;
import com.project.gongchalkka.member.dto.MemberSignupRequest;
import com.project.gongchalkka.member.dto.TokenReissueRequest;
import com.project.gongchalkka.member.dto.TokenResponse;
import com.project.gongchalkka.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(/*@Valid*/ @RequestBody MemberSignupRequest request) {

        Long memberId = memberService.signup(request);

        return ResponseEntity.created(URI.create("/members/" + memberId)).build();
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody MemberLoginRequest request) {
        TokenResponse tokenResponse = memberService.login(request);

        log.info("accessToken: {}, refreshToken: {}", tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());

        return ResponseEntity.ok(tokenResponse);
    }

    /**
     * 토큰 재발급
     */
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(@RequestBody TokenReissueRequest request) {
        TokenResponse tokenResponse = memberService.reissue(request);

        return ResponseEntity.ok(tokenResponse);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Principal principal) {
        memberService.logout(principal);

        return ResponseEntity.noContent().build();
    }
}
