<script setup>
import { useRouter } from 'vue-router'
import NavMenu from '@/components/NavMenu.vue';

const router = useRouter()

function loginWithNaver() {
  console.log("네이버 로그인 클릭")
  router.push('/menu')
}

function loginWithKakao() {
  console.log("카카오 로그인 클릭")
  router.push('/menu')
}
</script>

<template>
  <!-- 상단 NavMenu 유지 -->
  <NavMenu />

  <!-- 로그인 버튼 중앙 배치 -->
  <div
    class="login-container"
  >
    <div class="login-card">
      <h2 class="login-title">간편 로그인</h2>

      <button class="login-btn naver-btn" @click="loginWithNaver">
        <img src="https://static.nid.naver.com/oauth/small_g_in.PNG" alt="Naver" />
        네이버로 로그인
      </button>

      <button class="login-btn kakao-btn" @click="loginWithKakao">
        <img src="https://developers.kakao.com/tool/resource/static/img/button/login/full/ko/kakao_login_small.png" alt="Kakao" />
        카카오로 로그인
      </button>
    </div>
  </div>
</template>

<style scoped>
/* 화면 중앙 배치 */
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 64px); /* NavMenu 높이 제외 */
  background-color: inherit; /* 기존 배경 스타일 유지 */
}

/* 카드 스타일 */
.login-card {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 24px 32px;
  border-radius: 12px;
  background-color: rgba(255, 255, 255, 0.95);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  text-align: center;
  width: 400px;       /* 고정 너비 */
  max-width: 90%;     /* 화면이 작을 때는 90% 이하로 줄어듦 */
}

/* 제목 */
.login-title {
  margin-bottom: 8px;
  font-size: 20px;
  font-weight: 600;
  color: #333;
}

/* 버튼 공통 */
.login-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  height: 48px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  font-weight: 500;
  font-size: 16px;
  transition: all 0.2s;
}

/* 네이버 버튼 */
.naver-btn {
  background-color: #03C75A;
  color: #fff;
}

.naver-btn:hover {
  filter: brightness(0.9);
}

/* 카카오 버튼 */
.kakao-btn {
  background-color: #FEE500;
  color: #3C1E1E;
}

.kakao-btn:hover {
  filter: brightness(0.9);
}

/* 아이콘 이미지 */
.login-btn img {
  width: 20px;
  height: 20px;
}
</style>
