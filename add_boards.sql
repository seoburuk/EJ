-- 동아리 홍보 게시판 및 밥/스터디 모임 게시판 추가

-- 기존 게시판 확인
SELECT * FROM boards;

-- 동아리 홍보 게시판 추가
INSERT INTO boards (name, description, post_count, category, icon)
VALUES ('동아리 홍보', '동아리 및 학생 단체를 홍보하는 게시판입니다', 0, '커뮤니티', '🎪');

-- 밥 모임 게시판 추가
INSERT INTO boards (name, description, post_count, category, icon)
VALUES ('밥 모임', '함께 식사할 친구를 찾는 게시판입니다', 0, '모임', '🍚');

-- 스터디 모임 게시판 추가
INSERT INTO boards (name, description, post_count, category, icon)
VALUES ('스터디 모임', '스터디 그룹을 모집하거나 찾는 게시판입니다', 0, '모임', '📚');

-- 결과 확인
SELECT * FROM boards;
