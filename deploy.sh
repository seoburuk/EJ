#!/bin/bash

# Globalin Docker 배포 스크립트

set -e  # 오류 발생 시 스크립트 중단

echo "========================================="
echo "  Globalin 애플리케이션 Docker 배포"
echo "========================================="

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 함수: 성공 메시지
success() {
    echo -e "${GREEN}✓ $1${NC}"
}

# 함수: 경고 메시지
warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

# 함수: 에러 메시지
error() {
    echo -e "${RED}✗ $1${NC}"
}

# Docker 및 Docker Compose 확인
echo ""
echo "1. 환경 확인 중..."
if ! command -v docker &> /dev/null; then
    error "Docker가 설치되어 있지 않습니다."
    exit 1
fi
success "Docker 설치 확인"

if ! command -v docker-compose &> /dev/null; then
    error "Docker Compose가 설치되어 있지 않습니다."
    exit 1
fi
success "Docker Compose 설치 확인"

# 포트 사용 확인
echo ""
echo "2. 포트 사용 확인 중..."
if lsof -Pi :80 -sTCP:LISTEN -t >/dev/null 2>&1; then
    warning "포트 80이 사용 중입니다. 기존 프로세스를 종료해주세요."
    lsof -Pi :80 -sTCP:LISTEN
    read -p "계속하시겠습니까? (y/n) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

if lsof -Pi :8080 -sTCP:LISTEN -t >/dev/null 2>&1; then
    warning "포트 8080이 사용 중입니다. 기존 프로세스를 종료합니다..."
    lsof -ti:8080 | xargs kill -9 2>/dev/null || true
    sleep 2
    success "포트 8080 정리 완료"
fi

# 기존 컨테이너 정리
echo ""
echo "3. 기존 컨테이너 정리 중..."
docker-compose down 2>/dev/null || true
success "기존 컨테이너 정리 완료"

# Docker 이미지 빌드
echo ""
echo "4. Docker 이미지 빌드 중..."
echo "   이 작업은 몇 분 정도 소요될 수 있습니다..."
docker-compose build --no-cache
success "Docker 이미지 빌드 완료"

# 컨테이너 시작
echo ""
echo "5. 컨테이너 시작 중..."
docker-compose up -d
success "컨테이너 시작 완료"

# 컨테이너 상태 확인
echo ""
echo "6. 컨테이너 상태 확인 중..."
sleep 5
docker-compose ps

# 헬스 체크
echo ""
echo "7. 서비스 헬스 체크 중..."
echo "   백엔드 서비스 확인 중..."

# 백엔드 헬스 체크 (최대 60초 대기)
BACKEND_READY=0
for i in {1..12}; do
    if curl -s http://localhost:8080/api/health > /dev/null 2>&1; then
        BACKEND_READY=1
        break
    fi
    echo "   대기 중... ($i/12)"
    sleep 5
done

if [ $BACKEND_READY -eq 1 ]; then
    success "백엔드 서비스 정상 동작"
else
    error "백엔드 서비스 시작 실패"
    echo ""
    echo "백엔드 로그:"
    docker-compose logs backend
    exit 1
fi

# 프론트엔드 헬스 체크
echo "   프론트엔드 서비스 확인 중..."
if curl -s http://localhost/ > /dev/null 2>&1; then
    success "프론트엔드 서비스 정상 동작"
else
    warning "프론트엔드 서비스 접근 불가 (nginx 시작 대기 중일 수 있음)"
fi

# 완료
echo ""
echo "========================================="
echo -e "${GREEN}  배포 완료!${NC}"
echo "========================================="
echo ""
echo "접속 정보:"
echo "  - 프론트엔드: http://localhost"
echo "  - 백엔드 API: http://localhost:8080/api/health"
echo ""
echo "유용한 명령어:"
echo "  - 로그 확인: docker-compose logs -f"
echo "  - 백엔드 로그: docker-compose logs -f backend"
echo "  - 프론트엔드 로그: docker-compose logs -f frontend"
echo "  - 중지: docker-compose stop"
echo "  - 재시작: docker-compose restart"
echo "  - 제거: docker-compose down"
echo ""
