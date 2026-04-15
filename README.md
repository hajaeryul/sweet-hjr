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
