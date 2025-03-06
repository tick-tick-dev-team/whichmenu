# WhichMenu 프로젝트 🍽️

**WhichMenu**는 사용자가 원하는 메뉴를 확인하고, 관리할 수 있는 웹 애플리케이션입니다.

## 📁 프로젝트 구조
```plaintext
whichmenu/
├── whichmenu_frontend/   # 프론트엔드 (Vue 3 + Vite)
│   ├── src/
│   ├── public/
│   ├── package.json
│   └── ...
├── whichmenu_backend/    # 백엔드 (Spring Boot)
│   ├── src/
│   ├── build.gradle
│   ├── settings.gradle
│   └── ...
├── .gitignore
└── README.md
```

## 🚀 사용 기술
- **프론트엔드**: Vue 3 + Vite
- **백엔드**: Spring Boot
- **데이터베이스**: MySQL
- **버전 관리**: Git, GitHub

## 🛠️ 개발 환경 설정

### 버전
| 도구 | 버전 확인 명령어 | 현재 사용 버전 |
|------|----------------|---------------|
| Node.js | `node -v` | `v10.9.0` |
| npm | `npm -v` | `10.9.0` |
| Java | `java -version` | `23.0.1(Oracle JDK, HotSpot VM)` |
| Gradle | `gradle -v` |  |
| spring boot | `spring --version` | `3.4.3` |
| Vite | `npm list vue` | `3.5.13` |


### 프론트엔드 실행 방법
```sh
cd whichmenu_frontend
# 패키지가 설치되어 있지 않다면 npm install 명령어 실행
# 이미 한적이 있다면 npm update 실행 가능
npm run dev
```

### 백엔드 실행 방법
```sh
# 초기세팅  : whichmneu_backend 프로젝트 우클릭 > Gradle > Refresh Gradle Project
# 서버 실행 : 프로젝트 우클릭 > Run As > Spring Boot App
# 서버가 제대로 돌아가고 있는지 테스트 홈 화면 접속하기 "https://localhost:8080" > Console에 관리자 정보가 찍히면 DB까지 연결 성공
```
