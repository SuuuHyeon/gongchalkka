# ⚽ 공찰까 (Gongchalkka) - 백엔드
> **누구나 쉽게 풋살 매치를 찾고 참여할 수 있는 풋살장 예약 및 소셜 매칭 서비스의 백엔드 서버입니다.**

## 📌 프로젝트 개요
'공찰까' 백엔드는 실시간 매치 정보와 구장 데이터를 효율적으로 관리하며, 안정적인 매칭 시스템과 사용자 인증을 제공합니다.

## 🛠 기술 스택 (Tech Stack)
안정성과 성능, 그리고 생산성을 고려하여 구성된 백엔드 기술 스택입니다.

*   **언어:** Java 17
*   **프레임워크:** Spring Boot 3.5.x
*   **데이터베이스:** MySQL (RDB), Redis (Cache & Session)
*   **ORM:** Spring Data JPA
*   **메시징:** Spring Kafka
*   **보안 및 인증:** Spring Security, JWT (JJwt)
*   **문서화:** Swagger

## ✨ 주요 기능 (Features)
*   **⚽ 매치(Match) 서비스:** 매치 생성, 참가 신청, 실시간 매치 현황 관리 (Kafka 활용 가능성)
*   **🏟️ 풋살장(Field) 서비스:** 풋살장 정보 관리 및 위치 기반 데이터 제공
*   **👤 회원(Member) & 인증:** JWT 기반의 안전한 인증 시스템 및 회원 프로필 관리
*   **🛡️ 글로벌(Global) 설정:** 전역 예외 처리(Global Exception Handling), 보안 필터, 공통 유틸리티 제공
*   **📝 API 문서화:** Swagger UI를 통한 실시간 인터랙티브 API 가이드 제공
## 📂 폴더 구조 (Directory Structure)
도메인 주도 설계를 참고하여 직관적인 패키지 구조로 구성되어 있습니다.
```text
src/main/java/com/project/gongchalkka/
├── field/          # 🏟️ 풋살장 도메인 (Controller, Service, Repository, Entity)
├── global/         # 🛡️ 전역 설정 (Security, JWT, Config, Exception, Util)
├── match/          # ⚽ 매치 도메인 (Match, Subscription, Kafka Producers/Consumers)
├── member/         # 👤 회원 및 인증 도메인 (Auth, User, Social Login)
└── GongchalkkaApplication.java # Spring Boot 메인 실행 클래스


<!-- ```
 ## 🚀 시작하기 (Getting Started)
### 환경 설정 (.env)
루트 디렉토리에 `.env` 파일을 생성하고 필요한 환경 변수를 설정해야 합니다.
```env
# Example .env configuration
DB_URL=jdbc:mysql://localhost:3306/gongchalkka
DB_USERNAME=your_username
DB_PASSWORD=your_password
JWT_SECRET=your_secret_key
``` -->
<!-- ### 실행 방법
```bash
./gradlew bootRun
``` -->
<!-  ### API 문서 확인
서버 실행 후 브라우저에서 아래 주소로 접속하세요.
- Swagger UI: `http://localhost:8080/swagger-ui.html` -->
