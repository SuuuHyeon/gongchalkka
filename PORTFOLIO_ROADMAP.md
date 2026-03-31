# 🚀 포트폴리오 프로젝트 1-2달 완성 로드맵

**대상**: 개인 프로젝트 (포트폴리오)  
**기간**: 4-8주 (1-2달)  
**팀 규모**: 1명 (본인)  
**목표**: 채용공고 기반 완성도 높은 포트폴리오

---

## 📋 전체 타임라인

```
Week 1    : 기초 정비 (보안, 구조)
Week 2    : 테스트 작성 (단위 + 통합)
Week 3-4  : 성능 최적화 + 배포 준비
Week 5-6  : 프론트엔드 (Vue)
Week 7-8  : 완성 및 문서화

(탄력적: 프론트엔드 기간 조정 가능)
```

---

## 🎯 Week 1: 기초 정비 (5일 기준)

### 목표
- ✅ 보안 설정 완료
- ✅ 입력값 검증 완료
- ✅ 코드 정리
- ✅ 로깅 개선

### 상세 계획

#### **Day 1: 보안 강화 (2시간)**
```
1. SecurityConfig 수정
   ├─ public 엔드포인트 명확히 정의
   ├─ 비공개 엔드포인트에 @Secured 추가
   └─ CORS 설정 고도화

2. 입력값 검증
   ├─ MatchCreateRequest에 @Valid 추가
   ├─ MemberSignupRequest 추가 검증
   └─ MemberLoginRequest 검증

3. 민감 정보 로깅 제거
   ├─ 토큰 길이만 로깅
   ├─ 사용자 정보는 ID만 로깅
   └─ 조회 로깅 정책 수립
```

**파일 수정:**
- SecurityConfig.java
- MatchCreateRequest.java
- MemberSignupRequest.java
- MemberController.java (로깅)

---

#### **Day 2: 동시성 제어 (2시간)**
```
1. Match.java 동시성 제어
   ├─ addParticipant() synchronized 또는 @Version
   └─ removeParticipant() synchronized 또는 @Version

2. OptimisticLock 추가 (선택)
   ├─ Match에 @Version 필드 추가
   └─ 동시 수정 감지
```

**파일 수정:**
- Match.java

---

#### **Day 3: 에러 코드 & 예외처리 (2시간)**
```
1. ErrorCode 추가
   ├─ MATCH_TIME_INVALID 확인
   ├─ VALIDATION_FAILED 개선
   └─ 필요한 새 에러코드 추가

2. JwtTokenProvider 개선
   ├─ 예외 세분화 (ExpiredJwtException 등)
   └─ 명확한 에러 메시지
```

**파일 수정:**
- ErrorCode.java
- JwtTokenProvider.java

---

#### **Day 4-5: 코드 정리 & Swagger (3시간)**
```
1. TODO 주석 정리
   ├─ GitHub Issue 등록 후 주석 제거
   └─ 주석 처리 코드 정리

2. Swagger 어노테이션 추가
   ├─ Controller에 @Operation, @Parameter
   ├─ DTO에 @Schema
   └─ Swagger UI 테스트
```

**파일 수정:**
- 전체 Controller
- 전체 DTO

---

### 📊 Week 1 체크리스트

```
Day 1
☐ SecurityConfig 권한 설정
☐ 입력값 검증 추가
☐ 로깅 정리
☐ 테스트 실행

Day 2
☐ 동시성 제어 추가
☐ 데이터 무결성 테스트

Day 3
☐ 에러 코드 추가
☐ 예외 처리 개선
☐ 수동 테스트

Day 4-5
☐ TODO 정리
☐ Swagger 어노테이션
☐ API 문서 확인

예상 결과:
✓ 보안 설정 100% 완료
✓ 코드 품질 향상
✓ API 문서 자동 생성
```

---

## 🧪 Week 2: 테스트 작성 (10시간)

### 목표
- ✅ 단위 테스트 80% 이상
- ✅ 통합 테스트 주요 기능
- ✅ 테스트 커버리지 70% 이상

### 상세 계획

#### **테스트 구조**
```
src/test/java/com/project/gongchalkka/
├── member/
│   ├── service/
│   │   └── MemberServiceTest.java
│   ├── controller/
│   │   └── MemberControllerTest.java
│   └── entity/
│       └── MemberTest.java
├── match/
│   ├── service/
│   │   └── MatchServiceTest.java
│   ├── controller/
│   │   └── MatchControllerTest.java
│   └── entity/
│       └── MatchTest.java
├── global/
│   ├── jwt/
│   │   ├── JwtTokenProviderTest.java
│   │   └── CustomUserDetailsServiceTest.java
│   └── exception/
│       └── GlobalExceptionHandlerTest.java
└── integration/
    └── MatchIntegrationTest.java
```

#### **작성할 테스트**

**1. MemberServiceTest (3시간)**
```java
@SpringBootTest
class MemberServiceTest {
    
    // 회원가입 테스트
    @Test
    void testSignupSuccess() { ... }
    
    @Test
    void testSignupDuplicateEmail() { ... }
    
    // 로그인 테스트
    @Test
    void testLoginSuccess() { ... }
    
    @Test
    void testLoginInvalidPassword() { ... }
    
    // 토큰 재발급 테스트
    @Test
    void testReissueSuccess() { ... }
    
    @Test
    void testReissueInvalidToken() { ... }
    
    // 로그아웃 테스트
    @Test
    void testLogoutSuccess() { ... }
}
```

**2. MatchServiceTest (3시간)**
```java
@SpringBootTest
class MatchServiceTest {
    
    // 매치 생성 테스트
    @Test
    void testCreateMatchSuccess() { ... }
    
    @Test
    void testCreateMatchTimeConflict() { ... }
    
    // 매치 신청 테스트
    @Test
    void testApplyToMatchSuccess() { ... }
    
    @Test
    void testApplyToMatchCapacityFull() { ... }
    
    @Test
    void testApplyToMatchDuplicate() { ... }
    
    // 매치 취소 테스트
    @Test
    void testCancelMatchSuccess() { ... }
    
    @Test
    void testCancelMatchAlreadyCanceled() { ... }
}
```

**3. JWT 테스트 (2시간)**
```java
class JwtTokenProviderTest {
    
    @Test
    void testCreateAccessToken() { ... }
    
    @Test
    void testCreateRefreshToken() { ... }
    
    @Test
    void testValidateTokenSuccess() { ... }
    
    @Test
    void testValidateTokenExpired() { ... }
    
    @Test
    void testGetAuthenticationFromToken() { ... }
}
```

**4. 통합 테스트 (2시간)**
```java
@SpringBootTest
@AutoConfigureMockMvc
class MatchIntegrationTest {
    
    @Test
    void testSignupAndLoginFlow() { ... }
    
    @Test
    void testCreateMatchAndApplyFlow() { ... }
    
    @Test
    void testCancelMatchFlow() { ... }
    
    @Test
    void testUnauthorizedAccessBlocked() { ... }
}
```

### 📊 Week 2 체크리스트

```
☐ MemberServiceTest 작성 (3h)
☐ MatchServiceTest 작성 (3h)
☐ JWT 테스트 작성 (2h)
☐ 통합 테스트 작성 (2h)
☐ 테스트 커버리지 70% 달성
☐ 모든 테스트 통과

예상 결과:
✓ 단위 테스트: 80% 이상
✓ 통합 테스트: 주요 기능 완료
✓ 커버리지: 70% 이상
```

---

## ⚡ Week 3-4: 성능 최적화 + 배포 준비 (15시간)

### Week 3: 성능 최적화 (8시간)

#### **1. N+1 쿼리 최적화 (2시간)**
```
현재 상태:
- MatchRepository에 fetch join 있음 (좋음)
- 하지만 MatchSubscription 조회 시 추가 쿼리 가능

개선:
1. MatchRepository.findParticipantsByMatchId() 추가
   └─ fetch join으로 member 함께 로드

2. MatchService 메서드 검토
   └─ 불필요한 N쿼리 제거
```

**파일 수정:**
- MatchRepository.java
- MatchService.java

#### **2. 캐싱 전략 (3시간)**
```
1. Redis 캐싱 추가
   ├─ 매치 조회 결과 캐싱 (15분)
   ├─ 사용자 정보 캐싱 (30분)
   └─ 구장 정보 캐싱 (1시간)

2. application.yaml 설정
   ├─ Redis 연결
   └─ 캐시 TTL 설정

3. CacheConfig.java 추가
   └─ @EnableCaching 활성화
```

**파일 추가:**
- CacheConfig.java
- application.yaml 수정

#### **3. 데이터베이스 인덱싱 (2시간)**
```
인덱스 추가:
1. members 테이블
   └─ INDEX on email (고유성 + 검색)

2. match_subscriptions 테이블
   └─ INDEX on (member_id, match_id)
   └─ INDEX on (match_id, status)

3. matchs 테이블
   └─ INDEX on field_id
   └─ INDEX on start_time
   └─ INDEX on status
```

**파일 수정:**
- SQL Migration 또는 DataInitializer

#### **4. 성능 테스트 (1시간)**
```
1. 로드 테스트 도구
   └─ JMeter 또는 Gatling

2. 테스트 시나리오
   ├─ 회원가입 100건
   ├─ 동시 매치 신청 50건
   └─ 매치 조회 대량 요청

3. 결과 분석
   └─ 응답 시간 < 500ms (목표)
```

### Week 4: 배포 준비 (7시간)

#### **1. Docker 설정 (2시간)**
```
파일 생성:
1. Dockerfile
   ├─ JDK 17 기반
   ├─ Spring Boot jar 빌드
   └─ 포트 8080 노출

2. docker-compose.yml
   ├─ MySQL 서비스
   ├─ Redis 서비스
   └─ 백엔드 서비스

3. .dockerignore
   └─ 불필요한 파일 제외
```

**파일 추가:**
- Dockerfile
- docker-compose.yml
- .dockerignore

#### **2. CI/CD 설정 (3시간)**
```
GitHub Actions 설정:

파일: .github/workflows/main.yml

1. 빌드
   ├─ Gradle 빌드
   └─ 테스트 실행

2. 테스트
   ├─ JUnit 테스트
   └─ 커버리지 확인

3. 배포
   ├─ Docker 이미지 빌드
   └─ (선택) Docker Hub에 push
```

**파일 추가:**
- .github/workflows/main.yml

#### **3. 배포 스크립트 (2시간)**
```
배포 방법 3가지:

1. Docker로 로컬 실행
   └─ docker-compose up -d

2. AWS/GCP 배포 가이드
   └─ README에 작성

3. GitHub Actions 자동 배포
   └─ main branch push 시 자동 빌드
```

**파일 추가:**
- docs/DEPLOYMENT.md

---

## 🎨 Week 5-6: Vue.js 프론트엔드 (40시간)

### 구조 설계

```
frontend/
├── src/
│   ├── components/
│   │   ├── LoginForm.vue
│   │   ├── SignupForm.vue
│   │   ├── MatchList.vue
│   │   ├── MatchDetail.vue
│   │   ├── MatchCreate.vue
│   │   └── MyMatches.vue
│   │
│   ├── views/
│   │   ├── LoginPage.vue
│   │   ├── HomePage.vue
│   │   ├── MatchListPage.vue
│   │   └── MyPage.vue
│   │
│   ├── services/
│   │   ├── api.js (axios 설정)
│   │   ├── authService.js
│   │   └── matchService.js
│   │
│   ├── store/ (Vuex 또는 Pinia)
│   │   ├── modules/
│   │   │   ├── auth.js
│   │   │   └── match.js
│   │   └── index.js
│   │
│   └── App.vue
│
└── package.json
```

### 주요 기능

#### **Phase 1: 기본 UI (Week 5)**
```
1. 로그인/회원가입 페이지 (6h)
   ├─ 이메일, 비밀번호, 닉네임 입력
   ├─ 토큰 저장 (localStorage)
   └─ 입력값 검증

2. 매치 목록 페이지 (4h)
   ├─ 페이징
   ├─ 필터링 (선택)
   └─ 로딩 상태 표시

3. 매치 상세 페이지 (3h)
   ├─ 매치 정보 표시
   ├─ 신청 버튼
   └─ 취소 버튼

4. 네비게이션/레이아웃 (3h)
   ├─ 헤더 (로그인/로그아웃)
   └─ 라우팅
```

#### **Phase 2: 고급 기능 (Week 6)**
```
1. 매치 생성 페이지 (6h)
   ├─ 날짜/시간 선택
   ├─ 구장 선택
   ├─ 정원 설정
   └─ 폼 검증

2. 내 신청 매치 (4h)
   ├─ 신청한 경기 목록
   └─ 취소 기능

3. 내 주최 매치 (4h)
   ├─ 주최한 경기 목록
   ├─ 참가자 목록
   └─ 경기 수정/삭제

4. 마이 페이지 (4h)
   ├─ 프로필 정보
   ├─ 프로필 수정
   └─ 로그아웃

5. UI/UX 개선 (6h)
   ├─ CSS/Tailwind 스타일링
   ├─ 반응형 디자인
   └─ 에러 메시지 표시
```

### 📊 Week 5-6 체크리스트

```
Week 5
☐ Vue 프로젝트 생성
☐ 라우팅 설정
☐ API 서비스 설정
☐ 로그인/회원가입 UI (6h)
☐ 매치 목록 UI (4h)
☐ 매치 상세 UI (3h)
☐ 레이아웃/네비게이션 (3h)

Week 6
☐ 매치 생성 UI (6h)
☐ 내 신청 매치 (4h)
☐ 내 주최 매치 (4h)
☐ 마이 페이지 (4h)
☐ 스타일링 및 반응형 (6h)
☐ 에러 처리 및 로딩 상태

예상 결과:
✓ 모든 주요 기능 구현
✓ 반응형 디자인
✓ 백엔드 API 연동
```

---

## 📝 Week 7-8: 완성 및 문서화 (10시간)

### Week 7: 통합 및 버그 수정 (5시간)

#### **1. 프론트-백 연동 테스트 (2시간)**
```
1. 전체 기능 통합 테스트
   ├─ 회원가입 → 로그인 → 경기 신청 → 경기 취소
   └─ 모바일 환경 테스트

2. 버그 수정
   ├─ CORS 문제
   ├─ 토큰 만료 처리
   └─ 네트워크 오류 처리
```

#### **2. 성능 최적화 (1.5시간)**
```
1. 프론트엔드 성능
   ├─ 번들 크기 최소화
   ├─ 이미지 최적화
   └─ 번들 분석

2. 백엔드 최적화
   ├─ 느린 API 분석
   └─ 캐싱 개선
```

#### **3. 보안 검수 (1.5시간)**
```
1. 프론트엔드 보안
   ├─ XSS 방지 (Vue는 기본 제공)
   ├─ CSRF 토큰 (선택)
   └─ 민감 정보 보호

2. 백엔드 보안 재검수
   ├─ 모든 API 인증 확인
   └─ 입력값 검증 확인
```

### Week 8: 최종 문서화 (5시간)

#### **1. README.md 작성 (2시간)**
```
섹션:
1. 프로젝트 소개
   ├─ 목적
   ├─ 주요 기능
   └─ 스크린샷

2. 기술 스택
   ├─ 백엔드
   ├─ 프론트엔드
   └─ 배포

3. 설치 및 실행
   ├─ 요구 사항
   ├─ 설치 방법
   ├─ 환경 변수 설정
   └─ Docker로 실행

4. API 문서
   ├─ 인증 관련 API
   ├─ 매치 관련 API
   └─ 사용자 관련 API

5. 테스트
   ├─ 단위 테스트 실행 방법
   ├─ 통합 테스트 실행 방법
   └─ 커버리지 확인

6. 아키텍처
   ├─ 전체 구조
   ├─ 데이터베이스 다이어그램
   └─ API 흐름도

7. 문제 해결
   ├─ 자주 나는 문제
   └─ 해결 방법
```

#### **2. 배포 가이드 (1.5시간)**
```
docs/DEPLOYMENT.md 작성:

1. 로컬 환경
   └─ docker-compose up

2. 클라우드 배포 (AWS 예)
   ├─ RDS MySQL 설정
   ├─ ElastiCache Redis 설정
   ├─ EC2에 배포
   └─ CloudFront CDN

3. 모니터링
   ├─ CloudWatch로그
   └─ 알림 설정
```

#### **3. 코드 리뷰 및 정리 (1.5시간)**
```
1. 최종 코드 검수
   ├─ 코드 스타일 통일
   ├─ 주석 정리
   └─ 라이선스 표기

2. 깃허브 설정
   ├─ 좋은 README
   ├─ Topics 추가
   ├─ Description 작성
   └─ 라이선스 파일
```

---

## 📊 전체 일정표

```
Week 1 (Day 1-5)       : 기초 정비
├─ 보안 강화 (Day 1)
├─ 동시성 제어 (Day 2)
├─ 예외 처리 (Day 3)
└─ 코드 정리 & Swagger (Day 4-5)
예상 시간: 10시간

Week 2 (Day 6-10)      : 테스트 작성
├─ 단위 테스트 (3시간)
├─ 통합 테스트 (2시간)
└─ 테스트 커버리지 확인
예상 시간: 10시간

Week 3 (Day 11-15)     : 성능 최적화
├─ N+1 최적화
├─ 캐싱 추가
├─ 인덱싱
└─ 성능 테스트
예상 시간: 8시간

Week 4 (Day 16-20)     : 배포 준비
├─ Docker 설정
├─ CI/CD 파이프라인
└─ 배포 스크립트
예상 시간: 7시간

Week 5-6 (Day 21-40)   : 프론트엔드
├─ 기본 UI (Week 5)
└─ 고급 기능 (Week 6)
예상 시간: 40시간

Week 7 (Day 41-45)     : 통합 및 버그 수정
├─ 연동 테스트
├─ 성능 최적화
└─ 보안 검수
예상 시간: 5시간

Week 8 (Day 46-50)     : 문서화
├─ README 작성
├─ 배포 가이드
└─ 최종 정리
예상 시간: 5시간

┌────────────────────────────────┐
│ 전체: 약 85시간 (약 2-3주)    │
│ = 약 1.5-2달 (풀타임 기준)    │
└────────────────────────────────┘

탄력적 일정:
- 프론트엔드를 더 간단히 하면: 1달
- 더 정교하게 하려면: 2달
```

---

## 🎯 포트폴리오 최종 체크리스트

```
백엔드
┌─ 기능 (80점)
│  ├─ [✓] 회원 인증 (회원가입, 로그인, 로그아웃)
│  ├─ [✓] 매치 CRUD (생성, 조회, 신청, 취소)
│  ├─ [✓] 정원 관리
│  ├─ [?] 검색/필터링 (선택사항)
│  └─ [?] 리뷰/평점 (선택사항)
│
├─ 코드 품질 (90점)
│  ├─ [✓] 명확한 구조
│  ├─ [✓] 예외 처리
│  ├─ [✓] 로깅/모니터링
│  └─ [✓] 문서화
│
├─ 보안 (95점)
│  ├─ [✓] JWT 인증
│  ├─ [✓] 입력값 검증
│  ├─ [✓] 권한 제어
│  └─ [✓] SQL Injection 방지
│
├─ 성능 (90점)
│  ├─ [✓] N+1 최적화
│  ├─ [✓] 캐싱
│  ├─ [✓] 인덱싱
│  └─ [✓] 동시성 제어
│
├─ 배포 (95점)
│  ├─ [✓] Docker
│  ├─ [✓] CI/CD
│  ├─ [✓] 배포 가이드
│  └─ [✓] 모니터링
│
└─ 테스트 (85점)
   ├─ [✓] 단위 테스트
   ├─ [✓] 통합 테스트
   ├─ [✓] 커버리지 70%+
   └─ [?] E2E 테스트 (선택사항)

프론트엔드
├─ [✓] 회원가입/로그인
├─ [✓] 매치 목록 및 조회
├─ [✓] 매치 신청/취소
├─ [✓] 매치 생성
├─ [✓] 내 정보 관리
├─ [✓] 반응형 디자인
├─ [✓] 에러 처리
└─ [✓] 로딩 상태 표시

문서
├─ [✓] README.md
├─ [✓] API 문서 (Swagger)
├─ [✓] 배포 가이드
├─ [✓] 아키텍처 다이어그램
└─ [✓] 문제 해결 가이드

종합 평가
─────────────
백엔드: 90점 (좋음)
프론트엔드: 85점 (좋음)
문서: 95점 (매우 좋음)
배포: 95점 (매우 좋음)
────────────────
총점: 91점 (우수)
```

---

## 💡 추가 팁

### 우선순위가 높은 순서로 진행하기

```
🔴 필수 (이것 없으면 떨어짐):
1. 보안 설정 완료
2. 테스트 코드 80% 이상
3. Docker/배포 가능
4. API 문서화 완벽
5. README 명확함

🟡 중요 (있으면 훨씬 좋음):
1. 성능 최적화 (N+1, 캐싱)
2. 통합 테스트
3. CI/CD 파이프라인
4. 프론트엔드 연동

🟢 좋으면 더 좋음:
1. 리뷰/평점 기능
2. 검색/필터링
3. 실시간 알림
4. 모바일 앱
```

### 깃허브에 잘 보이도록 하기

```
1. README 상단에 스크린샷
2. 기술 스택을 명시적으로
3. 주요 기능 리스트업
4. 설치/실행 방법 명확
5. 배포 상태 배지
6. 테스트 커버리지 배지
7. License 명시
8. 기여 가이드 (있으면 더 좋음)
```

### 면접 대비

```
준비할 내용:
1. 프로젝트 전체 아키텍처 설명 (2분)
2. 기술 선택 이유 (왜 Spring Boot 3.x?)
3. 어려웠던 부분과 해결책
   └─ 동시성 제어? 성능 최적화? 보안?
4. 테스트 전략
5. 배포 방식
6. 개선하고 싶은 부분
7. 다시 할 것 같이 다한 부분
```

---

## 🚀 최종 목표

**이 계획을 따르면 1.5-2달 내 다음을 달성할 수 있습니다:**

1. ✅ 채용공고 기반 필수 기술 스택 모두 적용
2. ✅ 탄탄한 테스트와 높은 코드 품질
3. ✅ 프로덕션 배포 가능한 상태
4. ✅ 완벽한 문서화
5. ✅ 풀 스택 포트폴리오 (백엔드 + 프론트엔드)

**→ 중견~대기업 면접 통과 가능한 수준의 포트폴리오**


