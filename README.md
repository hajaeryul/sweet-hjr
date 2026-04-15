## 1. 서비스 소개
### 📌 한 줄 소개
팬이 직접 참여해 사진을 업로드하고, 큐레이션을 통해 포토북을 제작 및 주문 할 수 있는 플랫폼

## 🎯 타겟 고객
- 인플루언서 / 유튜버 / 크리에이터 / 스포츠 스타 등
- 해당 팬과 팬 커뮤니티 유저
- 굿즈 제작 및 팬 참여형 콘텐츠를 만들고 싶은 크리에이터

## ✨ 주요 기능
- 프로젝트 생성(인플루언서 기반)
- 팬 사진 업로드
- AI 기반 이미지 분석(품질/노이즈/텍스트 등)
- 유사 이미지 그룹핑 및 대표 이미지 선정
- 큐레이터 이미지 선정(인플루언서에게 수령한 희소성 있는 고화질 사진 포함)
  (아래부터는 BookPrint API 연동)
- 포토북 자동 생성
- 포토북 미리보기
- 포토북 주문 및 견적 확인

## 2. 실행 방법
### 📁 프로젝트 클론
```Bash
git clone https://github.com/hajaeryul/sweet-hjr.git
cd sweet-hjr
```

## 🛠 백엔드 실행 (Spring Boot)
### 1. DB 준비 (MySQL)
```SQL
CREATE DATABASE sweet_hjr;
```

### 2. 환경 설정 파일 생성
```Bash
cp server/src/main/resources/application-secret.yml.example server/src/main/resources/application-secret.yml
```
### 3. application-secret.yml 생성
```yaml
sweetbook:
  base-url: https://api-sandbox.sweetbook.com/v1
  api-key: YOUR_API_KEY_HERE
```

### 4. 서버 실행
```Bash
cd server
./mvnw spring-boot:run
```
또는 IntelliJ에서 실행

## 💻 프론트엔드 실행 (Next.js)
### 1. 의존성 설치
```bash
cd client
npm install
```
### 2. 실행
```bash
npm run dev
```
### 🌐 접속
```
http://localhost:3000
```
#### ⚠️ 주의사항
- BookPrint API Key 필요
- MySQL 실행 상태 필요
- 서버(8080) → 클라이언트(3000) 연결됨

## MYSQL 데이터베이스 생성
로컬 MYSQL에서 아래 명령으로 DB를 생성
```SQL
CREATE DATABASE sweet_hjr CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
최초 백엔드 실행 시 JPA가 테이블을 생성합니다

### 필수 초기 데이터 삽입(MySQL)
#### 1) 사용자 데이터 삽입
예시:
```SQL
INSERT INTO users (email, password_hash, nickname, status, created_at, updated_at)
VALUES ('admin@test.com', 'test-hash', '관리자', 'ACTIVE', NOW(), NOW());

INSERT INTO users (email, password_hash, nickname, status, created_at, updated_at)
VALUES ('user@test.com', 'test-hash', '유저', 'ACTIVE', NOW(), NOW());

INSERT INTO users (email, password_hash, nickname, status, created_at, updated_at)
VALUES ('curator@test.com', 'test-hash', '큐레이터', 'ACTIVE', NOW(), NOW());
```

#### 2) Role 데이터 삽입
```SQL
INSERT INTO user_roles (created_at, role_type, user_id)
VALUES (NOW(), 'ADMIN', 1);

INSERT INTO user_roles (created_at, role_type, user_id)
VALUES (NOW(), 'USER', 2);

INSERT INTO user_roles (created_at, role_type, user_id)
VALUES (NOW(), 'CURATOR', 3);
```

#### 3) 인플루언서 데이터 삽입
예시:
```SQL
INSERT INTO influencers (id, name, display_name, category, profile_image_url, status, created_at, updated_at)
VALUES
(1, 'FAKER', '페이커', 'SPORTS_STAR', 'https://test.jpg', 'ACTIVE', NOW(), NOW());
```

#### 4) 프로젝트 데이터 삽입
프로젝트를 생성한 사용자는 관리자 계정으로 가정합니다.
예시:
```SQL
INSERT INTO projects (
    id, influencer_id, created_by, title, description, cover_image_url, status,
    upload_start_at, upload_end_at,
    preview_start_at, preview_end_at,
    order_start_at, order_end_at,
    created_at, updated_at
) VALUES (
    1, 1, 1,
    '페이커 포토북 굿즈',
    '리그오브레전드의 전설 T1 페이커의 팬 참여형 포토북 프로젝트',
    'https://i.namu.wiki/i/0mzaGn7CkphuIcQJ-fR40ZY_MGz6TeW2tWvMiHv_OGSpQFhd8T7TbwIfVaiKZYTeBrhClmiFOdKG_PxTvur6wXEGIBDHtL76ykNx_vt2FK2Zu5ctyZn6xbDnCJwpB0mC3QB0leZrGAwh1PBuEB7s3Q.webp',
    'COLLECTING',
    '2026-04-10 00:00:00', '2026-04-20 23:59:59',
    '2026-04-21 00:00:00', '2026-04-27 23:59:59',
    '2026-04-28 00:00:00', '2026-05-05 23:59:59',
    NOW(), NOW()
);
```

#### 5) 주문 테스트를 위한 프로젝트 기간 조정
주문 기능을 테스트하려면 주문 기간이 현재 시각 기준으로 열려 있어야 합니다.
```SQL
UPDATE project
SET
    order_start_at = NOW() - INTERVAL 1 DAY,
    order_end_at = NOW() + INTERVAL 7 DAY
WHERE id = 1;
```

## 3. 사용한 API 목록
| API                                | 용도        |
| ---------------------------------- | --------- |
| POST /books                        | 포토북 생성    |
| GET /templates                     | 템플릿 목록 조회 |
| POST /books/{bookUid}/cover        | 표지 생성     |
| POST /books/{bookUid}/contents     | 내지 생성     |
| POST /books/{bookUid}/finalization | 포토북 최종화   |
| POST /orders/estimate              | 주문 견적 조회  |
| POST /orders                       | 포토북 주문 생성 |

## 4. AI 도구 사용 내역
| AI 도구   | 활용 내용                  |
| ------- | ---------------------- |
| ChatGPT | DB설계 및 구조 개선         |
| ChatGPT | 백엔드 API 설계 및 구조 개선     |
| ChatGPT | 프론트 UI 및 상태 흐름 설계      |
| ChatGPT | BookPrint API 연동 구조 설계 |
| ChatGPT | 이미지 큐레이션 로직 개선         |

## 5. 설계 의도
### 📌 서비스 선택 이유

단순한 포토북 제작이 아니라
<b>팬 참여형 콘텐츠 제작 경험</b>을 제공하는 서비스에 집중하였다.

### 💡 비즈니스 가능성
- 팬덤 기반 굿즈 시장과 결합 가능
- 인플루언서 커뮤니티 확장 가능
- 사용자 참여형 콘텐츠 플랫폼으로 발전 가능
 
### 🚀 추가로 구현하고 싶었던 기능
- UI/UX
- AI 기반 자동 레이아웃 구성
- 주문 내역 관리 (마이페이지)
- 웹훅 기반 주문 상태 동기화
