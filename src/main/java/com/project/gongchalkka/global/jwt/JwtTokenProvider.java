package com.project.gongchalkka.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long accessTokenValidityInMs;
    private final long refreshTokenValidityInMs;
    private final String AUTHORITIES_KEY = "auth";
    private final CustomUserDetailsService customUserDetailsService;

    /// yml에서 설정 값 주입
    public JwtTokenProvider(
            JwtProperties jwtProperties, CustomUserDetailsService customUserDetailsService
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());    // Base64로 인코딩된 비밀 키를 디코딩 후 'Key' 객체로 변환
        this.key = Keys.hmacShaKeyFor(keyBytes);                // HMAC-SHA 알고리즘으로 Key 생성
        // 3. yml의 값들을 객체에서 꺼내 씀
        this.accessTokenValidityInMs = jwtProperties.getAccessTokenExpirationMs();
        this.refreshTokenValidityInMs = jwtProperties.getRefreshTokenExpirationMs();

        this.customUserDetailsService = customUserDetailsService;

        log.info("Loaded JWT Secret Key (HMAC-SHA Key): {}", this.key.toString());
    }

    /// 엑세스 토큰 생성
    public String createAccessToken(Authentication authentication) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + accessTokenValidityInMs);


        String authorities = authentication.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.joining(","));

        log.info("authorities: {}", authorities);

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .subject(authentication.getName())                          // (Subject) = email
                .claim("id", customUserDetails.getMember().getId())  // (Claim) = memberId
                .claim(AUTHORITIES_KEY, Role.USER)                          // (Claim) = "ROLE_USER"
                .issuedAt(new Date(now))
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    /// 리프레쉬 토큰 생성
    public String createRefreshToken() {
        long now = (new Date()).getTime();
        Date validity = new Date(now + refreshTokenValidityInMs);

        return Jwts.builder()
                .issuedAt(new Date(now))
                .expiration(validity)
                .signWith(key)
                .compact();
    }


    /// 토근 검증 및 정보 추출
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(this.key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String email = claims.getSubject();     // 생성 때 넣었던 email

        List<SimpleGrantedAuthority> authorities = Arrays.stream((claims.get(AUTHORITIES_KEY)).toString().split(","))
                .map(
                        SimpleGrantedAuthority::new
                )
                .toList();

        // userDetails 정보 생성
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);


        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, "", authorities);

        log.info("토큰 검증 / 추출: {}", usernamePasswordAuthenticationToken);

        ///  TODO: 다시보기
        return usernamePasswordAuthenticationToken;
    }


    /// 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().verifyWith(this.key).build().parseSignedClaims(token);
            log.info("토큰 유효성 검사: {}", claimsJws.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
