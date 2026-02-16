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
- **iframe + postMessage 기반 SSO**: 쿠키에 저장된 토큰을 iframe으로 각 서비스에 전달
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
│   └── 인증: 포털에서 postMessage로 전달받은 JWT 토큰 검증
│
└── Persona AI (Service Module)
    ├── 포트: 8082
    ├── 역할: AI 캐릭터 학습 및 채팅
    └── 인증: 포털에서 postMessage로 전달받은 JWT 토큰 검증
```

### 멀티모듈 구조
```
portal/                  # MY AI 포털 (인증, 사용자 관리)
├── markdown-creator/    # Markdown Creator 서비스
└── persona/             # Persona AI 서비스
```

## 🔐 SSO 인증 방식

본 프로젝트는 **iframe + postMessage API**를 활용한 SSO(Single Sign-On)를 구현합니다.

### 인증 흐름
```
1. 포털(8080)에서 로그인
   ↓
2. JWT Access Token, Refresh Token 발급
   ↓
3. 토큰을 포털의 쿠키에 저장 (HttpOnly)
   ↓
4. 사용자가 메뉴 클릭 (예: Markdown Creator)
   ↓
5. 포털이 해당 서비스를 iframe으로 로드
   ↓
6. iframe 로드 완료 시 postMessage로 토큰 전달
   window.postMessage({
     type: 'AUTH_TOKEN',
     accessToken: '...',
     refreshToken: '...',
   }, 'http://localhost:8081')
   ↓
7. 각 서비스(8081, 8082)가 메시지 수신
   window.addEventListener('message', ...)
   ↓
8. 받은 토큰을 자신의 쿠키에 저장
   ↓
9. 이후 API 호출 시 저장된 토큰 사용
   Authorization: Bearer {accessToken}
```

### 포털 측 구현 (토큰 전달)
```typescript
// 포털 메인 페이지
function showService(serviceId: string) {
  const iframe = document.getElementById(serviceId) as HTMLIFrameElement;
  
  if (iframe) {
    iframe.onload = function() {
      const message = {
        type: 'AUTH_TOKEN',
        accessToken: getCookie('accessToken'),
        refreshToken: getCookie('refreshToken')
      };
      
      // iframe에 postMessage로 토큰 전달
      iframe.contentWindow?.postMessage(message, iframe.src);
    };
  }
}
```

### 서비스 측 구현 (토큰 수신)
```typescript
// 각 서비스 (8081, 8082)
window.addEventListener('message', function(event) {
  // ✅ 보안: 포털에서만 메시지 수신
  if (event.origin !== 'http://localhost:8080') {
    return;
  }
  
  if (event.data.type === 'AUTH_TOKEN') {
    // 받은 토큰을 자신의 쿠키에 저장
    document.cookie = `accessToken=${event.data.accessToken}; path=/; max-age=3600`;
    document.cookie = `refreshToken=${event.data.refreshToken}; path=/; max-age=604800`;
    
    // 인증 완료 처리
    initializeService();
  }
});
```

### 주요 특징

✅ **크로스 도메인 인증**: 포트가 다른 서비스 간에도 토큰 공유 가능  
✅ **보안**: origin 검증으로 신뢰할 수 있는 출처만 허용  
✅ **추가 인프라 불필요**: Redis 등 별도 세션 저장소 없이 구현  
✅ **투명한 SSO**: 사용자는 한 번 로그인 후 모든 서비스 자유롭게 이용

### 보안 고려사항

⚠️ **origin 검증 필수**: 메시지 수신 시 반드시 출처 확인
```typescript
if (event.origin !== 'http://localhost:8080') {
  return; // 차단
}
```

⚠️ **HttpOnly 쿠키**: 포털의 토큰은 HttpOnly 쿠키로 저장하여 XSS 공격 방지

⚠️ **HTTPS 필수**: 프로덕션 환경에서는 반드시 HTTPS 사용

## 🛠 기술 스택

### Backend
- **Spring Boot 3.5.5**: 기반 프레임워크
- **Spring Security**: 보안 및 인증
- **JWT (JSON Web Token)**: 토큰 기반 인증
- **JPA/Hibernate**: 데이터베이스 ORM
- **H2/MySQL**: 데이터베이스

### Frontend
- **React 18**: UI 라이브러리
- **TypeScript**: 타입 안정성
- **Vite**: 빌드 도구
- **React Router**: 라우팅

### 인증 방식
- **JWT Access Token**: 1시간 유효
- **JWT Refresh Token**: 7일 유효
- **쿠키 기반 토큰 저장**: HttpOnly 쿠키에 토큰 저장
- **iframe + postMessage**: 서비스 간 토큰 전달
- **자동 갱신**: Access Token 만료 시 자동으로 새 토큰 발급

### 보안
- **BCrypt**: 비밀번호 암호화
- **HTTPS**: 프로덕션 환경 통신 암호화
- **CORS**: Cross-Origin 요청 제어
- **Origin 검증**: postMessage 수신 시 출처 확인

## 🚀 시작하기

### 사전 요구사항
- JDK 24 이상
- Gradle 7.x 이상
- Node.js 18 이상
- AI API Key (OpenAI, Anthropic 등)

### 설치 및 실행

1. **저장소 클론**
```bash
git clone https://github.com/jbonnie/My-AI-Platform.git
cd myAI-platform
```

2. **환경변수 설정**
- src/main/resource 하위에 .env 파일 생성 필요
```env
JWT_SECRET=your-secret-key-at-least-256-bits-long
```

3. **백엔드 실행**
```bash
# 포털 실행 (8080)
./gradlew :portal:bootRun

# Markdown Creator 실행 (8081)
./gradlew :markdown-creator:bootRun

# Persona AI 실행 (8082)
./gradlew :persona:bootRun
```

4. **프론트엔드 실행**
```bash
cd ./frontend
npm install
npm run dev
```

5. **접속**
- 포털: http://localhost:8080
- Markdown Creator: http://localhost:8081 (포털 iframe 내부)
- Persona AI: http://localhost:8082 (포털 iframe 내부)

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
- 포털 토큰은 HttpOnly 쿠키에 저장하여 XSS 공격 방지
- 각 서비스는 postMessage로 받은 토큰을 자신의 쿠키에 저장
- 로그아웃 시 모든 쿠키 삭제로 즉시 접근 차단

### 비밀번호
- BCrypt 암호화 적용
- 최소 8자 이상 권장
- 회원탈퇴 시 비밀번호 재확인 필수

### postMessage 보안
- origin 검증으로 신뢰할 수 있는 출처만 메시지 수신
- 타입 체크로 예상된 메시지 형식만 처리
- 프로덕션 환경에서는 HTTPS 필수

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