# EveryJapan 프로젝트 구조

## 📁 전체 구조 개요

```
globalin/
├── backend/          # Spring Framework 기반 백엔드
├── frontend/         # React + TypeScript 기반 프론트엔드
├── docker-compose.yml
├── .gitignore
└── README.md
```

---

## 🔧 Backend 구조

### 📦 기술 스택
- Java 8
- Spring Framework
- MyBatis
- MariaDB

### 📂 디렉토리 구조 예시

```
backend/
├── Dockerfile
├── pom.xml
└── src/main/
    ├── java/com/globalin/
    │   ├── config/              # 설정 클래스
    │   │
    │   ├── controller/          # API 컨트롤러 레이어
    │   │   ├── AuthController.java
    │   │   ├── BoardController.java
    │   │   ├── CommentController.java
    │   │   ├── MessageController.java
    │   │   ├── MeetingController.java
    │   │   └── AdminController.java
    │   │
    │   ├── service/             # 비즈니스 로직 레이어
    │   │   ├── AuthService.java
    │   │   ├── BoardService.java
    │   │   ├── CommentService.java
    │   │   ├── MessageService.java
    │   │   ├── MeetingService.java
    │   │   └── AdminService.java
    │   │
    │   ├── dao/                 # 데이터 접근 계층 (MyBatis)
    │   │   ├── UserDao.java
    │   │   ├── BoardDao.java
    │   │   ├── CommentDao.java
    │   │   ├── MessageDao.java
    │   │   └── MeetingDao.java
    │   │
    │   ├── domain/              # 엔티티 & DTO
    │   │   ├── entity/          # 데이터베이스 엔티티
    │   │   │   ├── User.java
    │   │   │   ├── Board.java
    │   │   │   ├── Post.java
    │   │   │   ├── Comment.java
    │   │   │   ├── Message.java
    │   │   │   └── Meeting.java
    │   │   │
    │   │   └── dto/             # 데이터 전송 객체
    │   │       ├── request/     # 요청 DTO
    │   │       └── response/    # 응답 DTO
    │   │
    │   ├── exception/           # 예외 처리
    │   │   ├── GlobalExceptionHandler.java
    │   │   └── custom/          # 커스텀 예외
    │   │
    │   ├── filter/              # 서블릿 필터
    │   │   ├── CorsFilter.java
    │   │   └── AuthFilter.java
    │   │
    │   ├── interceptor/         # Spring 인터셉터
    │   │   └── AuthInterceptor.java
    │   │
    │   └── util/                # 유틸리티 클래스
    │       ├── JwtUtil.java     # (선택사항)
    │       ├── PasswordUtil.java
    │       └── EmailUtil.java
    │
    ├── resources/
    │   ├── mybatis/
    │   │   ├── mybatis-config.xml
    │   │   └── mapper/          # MyBatis 매퍼 XML
    │   │       ├── UserMapper.xml
    │   │       ├── BoardMapper.xml
    │   │       ├── CommentMapper.xml
    │   │       ├── MessageMapper.xml
    │   │       └── MeetingMapper.xml
    │   │
    │   ├── spring/
    │   │   ├── root-context.xml      # DB, MyBatis 설정
    │   │   └── servlet-context.xml   # DispatcherServlet 설정
    │   │
    │   ├── application.properties    # 애플리케이션 설정
    │   └── logback.xml               # 로깅 설정
    │
    └── webapp/WEB-INF/
        ├── web.xml              # 서블릿 설정
        └── views/               # JSP 뷰 (필요시)
```

---

## ⚛️ Frontend 구조

### 📦 기술 스택
- React
- TypeScript
- Docker

### 📂 디렉토리 구조

```
frontend/
├── Dockerfile
├── package.json
├── tsconfig.json
└── src/
    ├── api/                     # API 호출 함수
    │   ├── auth.ts
    │   ├── board.ts
    │   ├── comment.ts
    │   ├── message.ts
    │   └── meeting.ts
    │
    ├── components/              # 재사용 가능한 컴포넌트
    │   ├── common/              # 공통 컴포넌트
    │   │   ├── Header.tsx
    │   │   ├── Footer.tsx
    │   │   └── Layout.tsx
    │   │
    │   ├── board/               # 게시판 관련 컴포넌트
    │   ├── comment/             # 댓글 관련 컴포넌트
    │   └── meeting/             # 모임 관련 컴포넌트
    │
    ├── pages/                   # 페이지 컴포넌트
    │   ├── auth/                # 인증 관련 페이지
    │   │   ├── LoginPage.tsx
    │   │   ├── RegisterPage.tsx
    │   │   └── FindPasswordPage.tsx
    │   │
    │   ├── board/               # 게시판 페이지
    │   ├── admin/               # 관리자 페이지
    │   └── meeting/             # 모임 페이지
    │
    ├── hooks/                   # 커스텀 React 훅
    ├── store/                   # 상태 관리 (Redux/Zustand)
    ├── types/                   # TypeScript 타입 정의
    ├── utils/                   # 유틸리티 함수
    └── styles/                  # 스타일 파일
```

---

## 🏗️ 아키텍처 패턴

### Backend (MVC + Layered Architecture)
1. **Controller Layer** - HTTP 요청/응답 처리
2. **Service Layer** - 비즈니스 로직 수행
3. **DAO Layer** - 데이터베이스 접근 (MyBatis)
4. **Domain Layer** - 엔티티 및 DTO 정의

### Frontend (Component-Based)
1. **Pages** - 라우팅되는 최상위 페이지 컴포넌트
2. **Components** - 재사용 가능한 UI 컴포넌트
3. **API** - 백엔드와 통신하는 HTTP 클라이언트
4. **Store** - 전역 상태 관리

---

## 🐳 Docker 구성

프로젝트는 Docker Compose를 통해 다음과 같이 구성됩니다:
- Backend 컨테이너 (Spring + Tomcat)
- Frontend 컨테이너 (React + Node)
- Database 컨테이너 (MariaDB)

---

## 📝 주요 기능 모듈

### 1. 인증 (Auth)
- 로그인/회원가입
- 비밀번호 찾기
- 세션/토큰 관리

### 2. 게시판 (Board)
- 게시글 CRUD
- 게시판 카테고리 관리

### 3. 댓글 (Comment)
- 댓글 작성/수정/삭제
- 대댓글 지원

### 4. 쪽지 (Message)
- 회원 간 메시지 송수신

### 5. 모임 (Meeting)
- 모임 생성/관리
- 참가 신청

### 6. 관리자 (Admin)
- 회원 관리
- 게시글/댓글 관리
