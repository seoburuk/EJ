# Globalin ERD - 기능별 분류

## 목차
1. [사용자 관리 (User Management)](#1-사용자-관리)
2. [게시판 시스템 (Board System)](#2-게시판-시스템)
3. [쪽지 시스템 (Messaging System)](#3-쪽지-시스템)
4. [시간표 관리 (Timetable Management)](#4-시간표-관리)
5. [강의 평가 (Lecture Review)](#5-강의-평가)
6. [채팅 시스템 (Chat System)](#6-채팅-시스템)
7. [신고 시스템 (Report System)](#7-신고-시스템)
8. [알림 시스템 (Notification System)](#8-알림-시스템)

---

## 1. 사용자 관리 (User Management)

```mermaid
erDiagram
    users {
        bigint id PK "Auto Increment"
        varchar(20) username UK "고유, NOT NULL"
        varchar(255) password "암호화된 비밀번호"
        varchar(100) email UK "고유, NOT NULL"
        varchar(50) nickname "NOT NULL"
        varchar(255) avatar "프로필 이미지"
        text bio "자기소개"
        timestamp join_date "가입일시"
        timestamp last_login_date "마지막 로그인"
        varchar(20) status "ACTIVE/INACTIVE"
        varchar(20) role "USER/ADMIN"
    }
```

### 설명
- **핵심 기능**: 회원가입, 로그인, 프로필 관리
- **주요 필드**:
  - `username`: 4-20자 고유 아이디
  - `password`: BCrypt 암호화
  - `email`: 이메일 인증 및 아이디/비밀번호 찾기에 사용
  - `status`: 계정 활성화 상태 관리
  - `role`: 권한 관리 (일반 사용자/관리자)

---

## 2. 게시판 시스템 (Board System)

```mermaid
erDiagram
    boards ||--o{ posts : "has"
    users ||--o{ posts : "writes"
    posts ||--o{ comments : "has"
    posts ||--o{ post_likes : "receives"
    posts ||--o{ post_images : "contains"
    users ||--o{ comments : "writes"
    users ||--o{ post_likes : "gives"
    comments ||--o{ comment_likes : "receives"
    users ||--o{ comment_likes : "gives"

    boards {
        bigint id PK
        varchar(100) name "게시판 이름"
        text description "설명"
        varchar(50) category "카테고리"
        varchar(10) icon "아이콘"
        int post_count "게시글 수"
        timestamp created_at
        varchar(20) status "ACTIVE/INACTIVE"
    }

    posts {
        bigint id PK
        bigint board_id FK "게시판 ID"
        bigint author_id FK "작성자 ID"
        varchar(200) title "제목"
        text content "내용"
        int view_count "조회수"
        int like_count "좋아요 수"
        int comment_count "댓글 수"
        timestamp created_at
        timestamp updated_at
        varchar(20) status "ACTIVE/DELETED"
        tinyint is_notice "공지사항 여부"
        tinyint is_anonymous "익명 여부"
        int image_count "이미지 수"
    }

    comments {
        bigint id PK
        bigint post_id FK "게시글 ID"
        bigint author_id FK "작성자 ID"
        bigint parent_id FK "부모 댓글 ID(대댓글)"
        text content "내용"
        timestamp created_at
        timestamp updated_at
        varchar(20) status "ACTIVE/DELETED"
        tinyint is_anonymous "익명 여부"
        int like_count "좋아요 수"
    }

    post_likes {
        bigint id PK
        bigint post_id FK
        bigint user_id FK
        timestamp created_at
    }

    comment_likes {
        bigint id PK
        bigint comment_id FK
        bigint user_id FK
        timestamp created_at
    }

    post_images {
        bigint id PK
        bigint post_id FK
        varchar(500) image_url "이미지 URL"
        varchar(255) original_filename "원본 파일명"
        bigint file_size "파일 크기(bytes)"
        int display_order "표시 순서"
        timestamp created_at
    }
```

### 설명
- **핵심 기능**: 게시판별 게시글 작성/조회, 댓글, 좋아요, 이미지 업로드
- **주요 게시판**:
  - 중고장터, 자유게시판, 이벤트게시판
  - 동아리 홍보, 밥 모임, 스터디 모임 등
- **특징**:
  - 대댓글 지원 (`parent_id`)
  - 익명 게시 가능
  - 다중 이미지 업로드 지원
  - 공지사항 고정 기능

---

## 3. 쪽지 시스템 (Messaging System)

```mermaid
erDiagram
    users ||--o{ messages : "sends"
    users ||--o{ messages : "receives"

    messages {
        bigint id PK
        bigint sender_id FK "발신자 ID"
        bigint receiver_id FK "수신자 ID"
        varchar(200) title "제목"
        text content "내용"
        tinyint is_read "읽음 여부"
        timestamp created_at
        tinyint deleted_by_sender "발신자 삭제"
        tinyint deleted_by_receiver "수신자 삭제"
    }
```

### 설명
- **핵심 기능**: 사용자 간 1:1 쪽지 전송
- **특징**:
  - 읽음 표시 기능
  - 양방향 삭제 (발신자/수신자 각각 삭제 가능)
  - 읽지 않은 메시지 카운트

---

## 4. 시간표 관리 (Timetable Management)

```mermaid
erDiagram
    users ||--o{ timetables : "creates"
    timetables ||--o{ courses : "contains"

    timetables {
        bigint id PK
        bigint user_id FK "사용자 ID"
        varchar(20) semester "학기(봄/가을)"
        int year "년도"
        varchar(100) name "시간표 이름"
        tinyint is_default "기본 시간표 여부"
        timestamp created_at
        timestamp updated_at
    }

    courses {
        bigint id PK
        bigint timetable_id FK "시간표 ID"
        varchar(200) course_name "과목명"
        varchar(100) professor "교수명"
        varchar(100) location "강의실"
        int day_of_week "요일(0-6)"
        time start_time "시작 시간"
        time end_time "종료 시간"
        varchar(20) color "표시 색상"
        decimal credits "학점"
        text memo "메모"
        timestamp created_at
        timestamp updated_at
    }
```

### 설명
- **핵심 기능**: 개인 시간표 작성 및 관리
- **특징**:
  - 학기별 시간표 생성
  - 과목별 시간/요일 설정
  - 색상으로 과목 구분
  - 메모 기능으로 추가 정보 입력

---

## 5. 강의 평가 (Lecture Review)

```mermaid
erDiagram
    users ||--o{ lecture_reviews : "writes"

    lecture_reviews {
        bigint id PK
        bigint user_id FK "작성자 ID"
        varchar(200) course_name "과목명"
        varchar(100) professor "교수명"
        int difficulty "난이도(1-5)"
        int workload "과제량(1-5)"
        int satisfaction "만족도(1-5)"
        text review_text "리뷰 내용"
        varchar(20) semester "학기"
        int year "년도"
        tinyint is_anonymous "익명 여부"
        timestamp created_at
        timestamp updated_at
    }
```

### 설명
- **핵심 기능**: 강의에 대한 평가 및 리뷰 작성
- **평가 지표**:
  - 난이도 (1-5)
  - 과제량 (1-5)
  - 만족도 (1-5)
- **특징**:
  - 익명 리뷰 가능
  - 과목명과 교수명으로 검색
  - 학기별 필터링

---

## 6. 채팅 시스템 (Chat System)

```mermaid
erDiagram
    chat_messages {
        bigint id PK
        varchar(50) username "사용자명"
        text message "메시지 내용"
        timestamp created_at
    }
```

### 설명
- **핵심 기능**: 실시간 전체 채팅
- **특징**:
  - WebSocket 기반 실시간 통신
  - 전체 채팅방 (공개)
  - 메시지 히스토리 저장

---

## 7. 신고 시스템 (Report System)

```mermaid
erDiagram
    users ||--o{ reports : "files"
    posts ||--o{ reports : "reported"
    users ||--o{ reports : "reported"

    reports {
        bigint id PK
        bigint reporter_id FK "신고자 ID"
        bigint post_id FK "신고된 게시글 ID"
        bigint user_id FK "신고된 사용자 ID"
        text reason "신고 사유"
        varchar(20) status "pending/resolved"
        timestamp created_at
        timestamp resolved_at "처리 완료 시각"
        text admin_note "관리자 메모"
    }
```

### 설명
- **핵심 기능**: 게시글/사용자 신고 및 관리자 처리
- **신고 대상**:
  - 게시글 (`post_id`)
  - 사용자 (`user_id`)
- **처리 상태**:
  - pending: 대기중
  - resolved: 처리완료

---

## 8. 알림 시스템 (Notification System)

```mermaid
erDiagram
    users ||--o{ notifications : "receives"

    notifications {
        bigint id PK
        bigint user_id FK "수신자 ID"
        varchar(50) type "알림 타입"
        bigint related_id "관련 객체 ID"
        bigint actor_id "행동 주체 ID"
        varchar(100) actor_name "행동 주체 이름"
        text message "알림 메시지"
        tinyint is_read "읽음 여부"
        timestamp created_at
    }
```

### 설명
- **핵심 기능**: 사용자 활동에 대한 실시간 알림
- **알림 타입**:
  - 댓글 알림
  - 좋아요 알림
  - 쪽지 알림
  - 답글 알림
- **특징**:
  - 읽지 않은 알림 카운트
  - 실시간 알림 배지 표시
  - 관련 페이지로 바로 이동

---

## 전체 관계도 요약

```mermaid
erDiagram
    %% Core Entities
    users ||--o{ posts : "writes"
    users ||--o{ comments : "writes"
    users ||--o{ messages : "sends/receives"
    users ||--o{ timetables : "manages"
    users ||--o{ lecture_reviews : "writes"
    users ||--o{ reports : "files"
    users ||--o{ notifications : "receives"

    boards ||--o{ posts : "contains"
    posts ||--o{ comments : "has"
    posts ||--o{ post_images : "contains"
    timetables ||--o{ courses : "contains"

    %% Likes
    users ||--o{ post_likes : "likes posts"
    users ||--o{ comment_likes : "likes comments"
    posts ||--o{ post_likes : "receives"
    comments ||--o{ comment_likes : "receives"
```

---

## 데이터베이스 통계

| 기능 영역 | 테이블 수 | 주요 관계 |
|---------|----------|---------|
| 사용자 관리 | 1 | 모든 테이블과 연결 |
| 게시판 시스템 | 6 | users, boards 중심 |
| 쪽지 시스템 | 1 | users 양방향 |
| 시간표 관리 | 2 | users → timetables → courses |
| 강의 평가 | 1 | users → lecture_reviews |
| 채팅 시스템 | 1 | 독립적 |
| 신고 시스템 | 1 | users, posts 참조 |
| 알림 시스템 | 1 | users → notifications |

**전체**: 14개 테이블

---

## 주요 인덱스 정보

### users
- PK: `id`
- UK: `username`, `email`

### posts
- PK: `id`
- FK: `board_id`, `author_id`
- IDX: `created_at`, `status`

### comments
- PK: `id`
- FK: `post_id`, `author_id`, `parent_id`

### timetables
- PK: `id`
- FK: `user_id`
- IDX: `semester`

### lecture_reviews
- PK: `id`
- FK: `user_id`
- IDX: `course_name`, `professor`

### notifications
- PK: `id`
- FK: `user_id`
- IDX: `is_read`, `created_at`

---

## 기능별 주요 쿼리 패턴

### 게시판 시스템
```sql
-- 게시판별 최신 게시글 조회
SELECT p.*, u.nickname, b.name as board_name
FROM posts p
JOIN users u ON p.author_id = u.id
JOIN boards b ON p.board_id = b.id
WHERE p.status = 'ACTIVE' AND b.id = ?
ORDER BY p.created_at DESC
LIMIT 20;
```

### 알림 시스템
```sql
-- 읽지 않은 알림 개수
SELECT COUNT(*)
FROM notifications
WHERE user_id = ? AND is_read = 0;
```

### 강의 평가
```sql
-- 과목별 평균 평가
SELECT
    course_name,
    professor,
    AVG(difficulty) as avg_difficulty,
    AVG(workload) as avg_workload,
    AVG(satisfaction) as avg_satisfaction,
    COUNT(*) as review_count
FROM lecture_reviews
WHERE course_name = ? AND professor = ?
GROUP BY course_name, professor;
```

---

## 보안 및 제약사항

### 데이터 무결성
- 모든 FK는 CASCADE 또는 RESTRICT 설정
- `status` 필드로 논리 삭제 구현
- `created_at`, `updated_at` 자동 관리

### 개인정보 보호
- 비밀번호 BCrypt 암호화
- 익명 게시 옵션 제공
- 사용자 삭제 시 개인정보 익명화

### 성능 최적화
- 주요 검색 필드에 인덱스 설정
- 게시글/댓글 카운트 캐싱
- 페이지네이션 구현
