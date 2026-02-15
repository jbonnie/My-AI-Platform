# My AI Platform

AI 서비스들을 하나의 플랫폼에서 통합 관리하는 포털 서비스입니다.

## 📋 목차
- [프로젝트 개요](#프로젝트-개요)
- [주요 기능](#주요-기능)
- [서비스 구조](#서비스-구조)
- [기술 스택](#기술-스택)
- [시작하기](#시작하기)

## 🎯 프로젝트 개요

My AI Platform은 다양한 AI 기반 서비스를 하나의 통합 플랫폼에서 제공하는 포털입니다.
사용자는 한 번의 로그인으로 여러 AI 서비스를 자유롭게 이용할 수 있으며,
각 서비스는 독립적으로 운영되면서도 사용자 인증 정보를 공유합니다.
#### # 현재 제공 서비스
1. Markdown Creator
2. Persona AI

## ✨ 주요 기능

### 🔐 중앙 집중식 인증 시스템
- **JWT 기반 인증**: 포털에서 한 번 로그인하면 모든 하위 서비스 이용 가능
- **API Key 통합 관리**: 회원가입 시 사용자의 AI API Key를 등록하여 모든 서비스에서 공유
- **세션 기반 토큰 관리**: Access Token과 Refresh Token을 세션에 저장하여 안전하게 관리
- **자동 토큰 갱신**: Access Token 만료 시 Refresh Token을 통해 자동으로 갱신

### 📝 Markdown Creator
다양한 형식의 문서를 마크다운으로 변환하는 서비스입니다.

**지원 형식**
- Office 문서: `.ppt`, `.pptx`, `.doc`, `.docx`, `.xlsx`
- 텍스트 문서: `.txt`
- PDF 문서: `.pdf`

**주요 특징**
- ✅ AI 기반 변환으로 문서 내용 소실 없이 정확한 마크다운 생성
- ✅ 표, 서식 등을 마크다운 문법으로 정확하게 변환
- ✅ RAG(Retrieval-Augmented Generation) 학습에 최적화된 형식
- ✅ AI 학습 데이터로 바로 활용 가능

**활용 사례**
```
업무 문서 → Markdown Creator → 마크다운 → Persona AI 학습 데이터
```

### 🤖 Persona AI
AI에게 특정 캐릭터를 학습시켜 해당 캐릭터처럼 대화하는 서비스입니다.

**주요 특징**
- ✅ 문서 업로드를 통한 캐릭터 학습
- ✅ 학습된 캐릭터와 실시간 채팅
- ✅ 다양한 페르소나 생성 및 관리
- ✅ Markdown Creator와 연계하여 효과적인 학습 데이터 구축

**추천 워크플로우**
1. 캐릭터 관련 문서(대본, 소설, 설정집 등) 준비
2. Markdown Creator로 마크다운 형식으로 변환
3. Persona AI에 마크다운 문서 업로드
4. AI가 캐릭터를 학습하여 채팅 가능

## 🏗 서비스 구조
```
My AI Platform (Portal)
├── 포트: 8080
├── 역할: 중앙 인증 및 사용자 관리
├── 기능: 회원가입, 로그인, 로그아웃, API Key 관리
│
├── Markdown Creator (Service Module)
│   ├── 포트: 8081
│   ├── 역할: 문서 → 마크다운 변환
│   └── 인증: 포털 JWT 토큰 검증
│
└── Persona AI (Service Module)
    ├── 포트: 8082
    ├── 역할: AI 캐릭터 학습 및 채팅
    └── 인증: 포털 JWT 토큰 검증
```

### 멀티모듈 구조
```
myAI-platform/
├── portal/              # 포털 모듈 (인증, 사용자 관리)
├── markdown-creator/    # Markdown Creator 서비스
├── persona-ai/          # Persona AI 서비스
└── common/              # 공통 모듈 (JWT, Security 설정 등)
```

## 🛠 기술 스택

### Backend
- **Spring Boot 3.x**: 기반 프레임워크
- **Spring Security**: 보안 및 인증
- **JWT (JSON Web Token)**: 토큰 기반 인증
- **JPA/Hibernate**: 데이터베이스 ORM
- **H2/MySQL**: 데이터베이스

### 인증 방식
- **JWT Access Token**: 1시간 유효
- **JWT Refresh Token**: 7일 유효
- **세션 기반 토큰 저장**: 서버 측 세션에 토큰 저장
- **자동 갱신**: Access Token 만료 시 자동으로 새 토큰 발급

### 보안
- **BCrypt**: 비밀번호 암호화
- **HTTPS**: 프로덕션 환경 통신 암호화
- **CORS**: Cross-Origin 요청 제어

## 🚀 시작하기

### 사전 요구사항
- JDK 24 이상
- Gradle 7.x 이상
- AI API Key (OpenAI, Anthropic 등)

### 설치 및 실행

1. **저장소 클론**
```bash
git clone https://github.com/jbonnie/My-AI-Platform.git
cd myAI-platform
```

2. **환경변수 설정**
- src/main/resource 하위에 .env 파일 생성 필요
```
JWT_SECRET=your-secret-key-at-least-256-bits-long
```

3. **프론트엔드 실행**
```bash
cd ./frontend
npm install
npm run dev
```

4. **접속**
- 포털: http://localhost:8080
- Markdown Creator: http://localhost:8081
- Persona AI: http://localhost:8082

### 서비스 API 인증

모든 하위 서비스 API는 헤더에 Access Token을 포함해야 합니다:
```http
GET /api/service/endpoint
Authorization: Bearer {accessToken}
```

## 🔒 보안 고려사항

### API Key 관리
- 사용자의 AI API Key는 데이터베이스에 암호화되어 저장
- API Key는 서버 측에서만 처리되며 클라이언트에 직접 노출되지 않음

### 토큰 관리
- Access Token: 짧은 유효기간(1시간)으로 보안 강화
- Refresh Token: 긴 유효기간(7일)으로 사용자 편의성 제공
- 토큰은 세션에 저장되어 페이지 이동 시에도 유지
- 로그아웃 시 세션 무효화로 즉시 접근 차단

### 비밀번호
- BCrypt 암호화 적용
- 최소 8자 이상 권장
- 회원탈퇴 시 비밀번호 재확인 필수

## 🎯 활용 시나리오

### 시나리오 1: 업무 문서 AI 학습
```
1. 회사 업무 매뉴얼(PDF, DOCX)을 Markdown Creator에 업로드
2. 마크다운으로 변환된 문서 다운로드
3. Persona AI에 마크다운 문서 업로드
4. "회사 업무 도우미" 페르소나 생성
5. 직원들이 업무 관련 질문을 AI 도우미에게 채팅으로 문의
```

### 시나리오 2: 캐릭터 챗봇 제작
```
1. 소설 원고나 캐릭터 설정집을 Markdown Creator로 변환
2. Persona AI에 캐릭터 학습 데이터 업로드
3. 해당 캐릭터 페르소나로 팬들과 채팅 서비스 제공
```

### 시나리오 3: 교육 콘텐츠 제작
```
1. 교육 자료(PPT, PDF)를 마크다운으로 변환
2. AI에게 "강사" 페르소나 학습
3. 학생들이 AI 강사와 Q&A 세션 진행
```

## 🤝 기여하기

프로젝트에 기여하고 싶으시다면:
1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

**❤️ by 장보경 (/w Claude, Chat GPT, Gemini...)**