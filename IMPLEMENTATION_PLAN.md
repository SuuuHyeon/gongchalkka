# 🚀 Gongchalkka 프로젝트 개선 실행 계획

## 1️⃣ 즉시 해결할 항목 (우선순위 🔴 높음)

### A. 보안 설정 완성 (예상: 2-3시간)

**현재 문제:**
```
❌ SecurityConfig.java
.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
```

**해결 방법:**
```java
// SecurityConfig.java 수정 필요
.authorizeHttpRequests(auth ->
    auth.requestMatchers(
            "/api/members/signup",
            "/api/members/login",
            "/api/members/reissue",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**"
        ).permitAll()
        .anyRequest().authenticated()  // ← 나머지는 인증 필수
)
```

**영향도:** 🔴 심각 (보안 취약점)

---

### B. MatchCreateRequest 입력값 검증 추가 (예상: 1-2시간)

**현재 상태:**
```java
@Getter
public class MatchCreateRequest {
    /// TODO: 유효성 체크 추가  ← 이 부분!
    private Long fieldId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int maxCapacity;
}
```

**해결 방법:**
```java
@Getter
public class MatchCreateRequest {
    @NotNull(message = "구장 ID는 필수입니다.")
    private Long fieldId;
    
    @NotNull(message = "시작 시간은 필수입니다.")
    @FutureOrPresent(message = "시작 시간은 현재 이후여야 합니다.")
    private LocalDateTime startTime;
    
    @NotNull(message = "종료 시간은 필수입니다.")
    private LocalDateTime endTime;
    
    @Min(value = 1, message = "최대 인원은 1명 이상이어야 합니다.")
    @Max(value = 100, message = "최대 인원은 100명 이하여야 합니다.")
    private int maxCapacity;
}
```

**Service에 검증 로직 추가:**
```java
@Transactional
public MatchResponse createMatch(MatchCreateRequest request, Member member) {
    // startTime < endTime 검증
    if (!request.getStartTime().isBefore(request.getEndTime())) {
        throw new BusinessErrorException(ErrorCode.MATCH_TIME_INVALID);
    }
    
    // ... 나머지 로직
}
```

**영향도:** 🔴 심각 (데이터 무결성)

---

### C. 민감 정보 로깅 제거 (예상: 30분)

**문제 코드:**
```java
// MemberController.java
log.info("accessToken: {}, refreshToken: {}", 
         tokenResponse.getAccessToken(),  // 😱 토큰 전체 노출!
         tokenResponse.getRefreshToken());
```

**수정:**
```java
log.info("사용자 로그인 성공 - 토큰 발급 완료");
// 또는
log.debug("accessToken issued: length={}, refreshToken issued: length={}",
          tokenResponse.getAccessToken().length(),
          tokenResponse.getRefreshToken().length());
```

**영향도:** 🟡 중요 (보안)

---

## 2️⃣ 1주일 내 처리할 항목 (우선순위 🟡 중요)

### A. 토큰 만료 예외 세분화 (예상: 2시간)

**현재 코드:**
```java
public boolean validateToken(String token) {
    try {
        Jwts.parser().verifyWith(this.key).build()
            .parseSignedClaims(token);
        return true;
    } catch (Exception e) {
        return false;  // 모든 예외를 같은 방식으로 처리!
    }
}
```

**개선 방안:**
```java
public boolean validateToken(String token) {
    try {
        Jwts.parser().verifyWith(this.key).build()
            .parseSignedClaims(token);
        return true;
    } catch (ExpiredJwtException e) {
        log.warn("만료된 JWT 토큰: {}", e.getMessage());
        return false;
    } catch (MalformedJwtException e) {
        log.warn("잘못된 JWT 형식: {}", e.getMessage());
        return false;
    } catch (SecurityException e) {
        log.warn("JWT 서명 검증 실패: {}", e.getMessage());
        return false;
    } catch (Exception e) {
        log.warn("JWT 토큰 검증 실패: {}", e.getMessage());
        return false;
    }
}

// 또는 더 나은 방법: 예외를 던지고 Controller에서 처리
public Claims validateAndParseToken(String token) {
    try {
        return Jwts.parser().verifyWith(this.key).build()
                   .parseSignedClaims(token)
                   .getPayload();
    } catch (ExpiredJwtException e) {
        throw new BusinessErrorException(ErrorCode.TOKEN_EXPIRED);
    } catch (MalformedJwtException | SignatureException e) {
        throw new BusinessErrorException(ErrorCode.TOKEN_INVALID);
    }
}
```

---

### B. N+1 쿼리 최적화 (예상: 3시간)

**현재 상황:**
```
MatchController가 MatchResponse로 변환할 때,
field.getFieldName()을 호출하면 추가 쿼리 발생 가능
```

**확인 방법:**
```
application.yaml에 다음 추가:
spring:
  jpa:
    properties:
      hibernate:
        generate_statistics: true
logging:
  level:
    org.hibernate.stat: debug
```

**해결 방법:**
```java
// MatchRepository에 이미 fetch join이 있으므로 좋음
@Query("select m from Match m join fetch m.field join fetch m.host")
Page<Match> findAllWithField(Pageable pageable);

// MatchSubscription 조회 시에도 fetch join 추가
@Query("select ms from MatchSubscription ms join fetch ms.match join fetch ms.member")
List<MatchSubscription> findByMatchIdWithDetails(Long matchId);
```

---

### C. 에러 코드 추가 (예상: 1시간)

**필요한 새로운 에러코드:**
```java
// ErrorCode.java에 추가
MATCH_TIME_INVALID(HttpStatus.BAD_REQUEST, "M-009", "종료 시간은 시작 시간 이후여야 합니다."),

// 이미 있는지 확인 필요
// ...현재 ErrorCode 살펴보기
```

---

## 3️⃣ 2주일 내 구현할 기능

### A. 매치 참가자 목록 조회 API (예상: 3시간)

**새로운 엔드포인트:**
```
GET /api/matches/{matchId}/participants
```

**DTO 작성:**
```java
@Getter
@Builder
public class ParticipantResponse {
    private Long memberId;
    private String nickname;
    private String email;
    private LocalDateTime appliedAt;
}
```

**Repository 쿼리:**
```java
@Query("select new com.project.gongchalkka.match.dto.ParticipantResponse(" +
       "m.id, m.nickname, m.email, ms.createdAt) " +
       "from MatchSubscription ms " +
       "join ms.member m " +
       "where ms.match.id = :matchId " +
       "and ms.status = 'APPLIED'")
List<ParticipantResponse> findParticipantsByMatchId(@Param("matchId") Long matchId);
```

---

### B. 내 신청 매치 조회 API (예상: 2시간)

**새로운 엔드포인트:**
```
GET /api/matches/my-applications
```

**로직:**
```
현재 사용자의 모든 MatchSubscription 조회
Status = APPLIED인 것만 필터
```

---

### C. 내 주최 매치 조회 API (예상: 1.5시간)

**새로운 엔드포인트:**
```
GET /api/matches/my-hosted
```

**로직:**
```
현재 사용자가 host인 모든 Match 조회
```

---

## 4️⃣ 향후 3개월 로드맵

```
┌─────────────────────────────────────────────────┐
│         분기별 개발 계획                         │
└─────────────────────────────────────────────────┘

Month 1 (3월-4월)
├─ [x] 현재 분석 완료
├─ [ ] Phase 1: 보안 강화 (1-2주)
│   └─ 보안 설정, 검증, 로깅 정리
├─ [ ] Phase 2-1: 기능 확대 (1-2주)
│   └─ 매치 조회, 사용자 프로필 관리
└─ [ ] 테스트 시작

Month 2 (4월-5월)
├─ [ ] Phase 2-2: 검색 및 필터링
├─ [ ] Phase 3: Kafka 알림 시스템
├─ [ ] DB 성능 최적화
└─ [ ] 통합 테스트

Month 3 (5월-6월)
├─ [ ] Phase 4: 리뷰/평점 시스템
├─ [ ] Phase 5: Redis 캐싱
├─ [ ] 모든 테스트 커버리지 80% 이상
├─ [ ] Docker 컨테이너화
└─ [ ] 베타 배포

6월 말
├─ [ ] 최종 리뷰
├─ [ ] 성능 테스트
└─ [ ] 프로덕션 준비
```

---

## 5️⃣ 코드 스타일 및 규약

### 변경이 필요한 항목

**1. 주석 처리된 코드 정리**
```java
// 제거 대상:
//                 auth.requestMatchers(
//                                "/",
//                                "/index.html",
//                                "/app.js",
//                 )
// ...

// 유지할 것:
// TODO: 매치 생성 관리자 제한(보류)
```

**2. TODO 주석 정리**
```
현재 TODO:
1. MatchCreateRequest에 유효성 체크 추가
2. SecurityConfig 다시 보기
3. JwtTokenProvider 다시 보기

이들을 이슈로 등록하고 주석 제거 추천
```

**3. 로깅 레벨 적절화**
```
DEBUG:   개발 정보 (토큰 상세, 쿼리 등)
INFO:    중요 비즈니스 로직 (로그인, 매칭 신청)
WARN:    경고 (토큰 만료, 인증 실패)
ERROR:   심각한 에러 (DB 연결 실패)
```

---

## 6️⃣ 테스트 작성 계획

### Unit Test (개별 메서드)
```
Member 관련:
- ✅ 회원가입 (정상, 중복)
- ✅ 로그인 (정상, 실패)
- ⚠️  비밀번호 암호화

Match 관련:
- ✅ addParticipant() (정원 추가, 가득 참)
- ✅ removeParticipant() (정상 취소)
- ⚠️  동시성 테스트
```

### Integration Test (API 수준)
```
- POST /api/members/signup → 201
- POST /api/members/login → 200 + Token
- POST /api/matches → 201
- POST /api/matches/{id}/apply → 201
- DELETE /api/matches/{id} → 204
```

### 추가 필요 테스트
```
- JWT 토큰 만료 시 처리
- 중복 신청 방지
- 시간 중복 매치 방지
- 비정상 입력값 처리
```

---

## 7️⃣ 배포 전 체크리스트

```
┌─────────────────────────────────────────┐
│        프로덕션 배포 전 체크              │
└─────────────────────────────────────────┘

보안 (Security)
─ [ ] 모든 민감한 정보 로깅 제거
─ [ ] JWT_SECRET_KEY .env로 관리
─ [ ] CORS 설정 적절히 제한
─ [ ] SQL Injection 방지 (모두 JPA)
─ [ ] HTTPS 적용

성능 (Performance)
─ [ ] DB 인덱싱 추가
─ [ ] 느린 쿼리 최적화
─ [ ] 캐싱 (Redis)
─ [ ] 라이브 로드 테스트

안정성 (Stability)
─ [ ] 예외 처리 완벽
─ [ ] 롤백 계획 수립
─ [ ] 에러 알림 설정
─ [ ] 모니터링 설정

문서화 (Documentation)
─ [ ] API 문서 완성
─ [ ] 배포 가이드 작성
─ [ ] 운영 가이드 작성
─ [ ] 트러블슈팅 가이드 작성

테스트 (Testing)
─ [ ] 단위 테스트 80% 이상
─ [ ] 통합 테스트 완료
─ [ ] 성능 테스트 완료
─ [ ] 보안 테스트 완료
```

---

## 8️⃣ 팀 협력 방식

### 브랜치 전략 (Git Flow)

```
main (프로덕션)
 │
 └── release (배포 준비)
 │
 └── develop (개발 메인)
      │
      ├── feature/security (보안)
      ├── feature/validation (검증)
      ├── feature/search (검색)
      └── feature/notifications (알림)
```

### 코드 리뷰 기준

```
반드시 확인할 항목:
1. 보안: 민감 정보 노출 여부
2. 성능: N+1 쿼리, 동시성
3. 테스트: 신규 기능 테스트 포함
4. 문서: 변경사항 문서화
5. 스타일: 팀 규약 준수
```

---

## 9️⃣ 비용-편익 분석

| 항목 | 투자 시간 | 기대 효과 | 우선순위 |
|------|----------|---------|---------|
| 보안 설정 | 3시간 | 심각한 보안 취약점 해결 | 🔴 최고 |
| 입력값 검증 | 2시간 | 데이터 무결성 보장 | 🔴 최고 |
| 토큰 예외 처리 | 2시간 | UX 향상 | 🟡 높음 |
| N+1 최적화 | 3시간 | 성능 향상 | 🟡 높음 |
| Redis 적용 | 6시간 | 스케일링 용이 | 🟢 중간 |
| 테스트 작성 | 20시간 | 안정성 향상 | 🔴 필수 |

---

## 🔟 질문 및 의사결정 사항

```
1. 결제 기능이 필수인가?
   → 필수라면 결제 게이트웨이 선정 필요

2. 실시간 기능이 필수인가?
   → WebSocket, Server-Sent Events 등 검토 필요

3. 모바일 앱도 개발할 예정인가?
   → REST API 설계 재검토 필요

4. 언제부터 프로덕션 배포할 것인가?
   → 로드맵 재조정 필요

5. 팀 규모는?
   → 개발 분담 방식 결정 필요
```

---

**이 실행 계획을 따라 진행하면, 6개월 내 완성도 높은 플랫폼을 구축할 수 있습니다.**

