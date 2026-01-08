# Globalin Docker 배포 가이드

## 사전 요구사항

- Docker 20.10 이상
- Docker Compose 2.0 이상

## 빠른 시작

### 1. 전체 애플리케이션 실행

```bash
# 프로젝트 루트 디렉토리에서 실행
docker-compose up -d
```

### 2. 로그 확인

```bash
# 전체 로그 확인
docker-compose logs -f

# 백엔드 로그만 확인
docker-compose logs -f backend

# 프론트엔드 로그만 확인
docker-compose logs -f frontend
```

### 3. 애플리케이션 접속

- 프론트엔드: http://localhost
- 백엔드 API: http://localhost:8080/api/health

### 4. 중지 및 제거

```bash
# 컨테이너 중지
docker-compose stop

# 컨테이너 중지 및 제거
docker-compose down

# 컨테이너, 볼륨, 이미지 모두 제거
docker-compose down -v --rmi all
```

## 개별 서비스 빌드 및 실행

### 백엔드만 실행

```bash
# 백엔드 디렉토리로 이동
cd backend

# Docker 이미지 빌드
docker build -t globalin-backend .

# 컨테이너 실행
docker run -d -p 8080:8080 --name globalin-backend globalin-backend

# 로그 확인
docker logs -f globalin-backend
```

### 프론트엔드만 실행

```bash
# 프론트엔드 디렉토리로 이동
cd frontend

# Docker 이미지 빌드
docker build -t globalin-frontend .

# 컨테이너 실행
docker run -d -p 80:80 --name globalin-frontend globalin-frontend

# 로그 확인
docker logs -f globalin-frontend
```

## 개발 모드

개발 중에는 Docker 대신 로컬에서 실행하는 것이 좋습니다:

```bash
# 백엔드 (포트 8080)
cd backend
./mvnw tomcat7:run

# 프론트엔드 (포트 3000)
cd frontend
npm start
```

## 프로덕션 배포

### 환경 변수 설정

프로덕션 환경에서는 `.env` 파일을 생성하여 환경 변수를 관리할 수 있습니다:

```bash
# .env 파일 생성
cat > .env << EOF
# Backend
BACKEND_PORT=8080
JAVA_OPTS=-Xmx1g -Xms512m

# Frontend
FRONTEND_PORT=80

# Timezone
TZ=Asia/Tokyo
EOF
```

그런 다음 docker-compose.yml에서 환경 변수를 참조하도록 수정합니다.

### 이미지 최적화

빌드 시간을 줄이려면 Docker 빌드 캐시를 활용하세요:

```bash
# 캐시 사용하여 빌드
docker-compose build

# 캐시 없이 새로 빌드
docker-compose build --no-cache
```

## 문제 해결

### 포트 충돌

다른 애플리케이션이 이미 포트를 사용 중인 경우:

```bash
# 포트 사용 확인 (macOS/Linux)
lsof -i :8080
lsof -i :80

# 프로세스 종료
kill -9 <PID>
```

### 컨테이너 재시작

```bash
# 특정 서비스 재시작
docker-compose restart backend
docker-compose restart frontend

# 전체 재시작
docker-compose restart
```

### 디버깅

```bash
# 컨테이너 내부 접속
docker exec -it globalin-backend /bin/bash
docker exec -it globalin-frontend /bin/sh

# 컨테이너 상태 확인
docker-compose ps

# 리소스 사용량 확인
docker stats
```

## 헬스 체크

각 서비스는 헬스 체크가 구성되어 있습니다:

- 백엔드: `http://localhost:8080/api/health`
- 프론트엔드: `http://localhost/`

헬스 체크 상태 확인:

```bash
docker-compose ps
```

## 네트워크

컨테이너들은 `globalin-network`라는 브리지 네트워크를 통해 통신합니다.

프론트엔드에서 백엔드로의 API 요청은 nginx 프록시를 통해 전달됩니다.

## 보안 고려사항

1. 프로덕션 환경에서는 HTTPS를 사용하세요
2. 환경 변수로 민감한 정보를 관리하세요
3. 정기적으로 Docker 이미지를 업데이트하세요
4. 최소 권한 원칙을 적용하세요

## 성능 튜닝

### 백엔드 JVM 옵션

```yaml
environment:
  - JAVA_OPTS=-Xmx2g -Xms1g -XX:+UseG1GC -XX:MaxGCPauseMillis=200
```

### Nginx 캐싱

nginx.conf 파일을 수정하여 캐싱 정책을 조정할 수 있습니다.

