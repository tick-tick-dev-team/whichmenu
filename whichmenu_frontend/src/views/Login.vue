<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import NavMenu from '@/components/NavMenu.vue';

const router = useRouter()
const NAVER_CLIENT_ID    = import.meta.env.VITE_NAVER_CLIENT_ID;
const NAVER_CALLBACK_URL = import.meta.env.VITE_NAVER_CALLBACK_URL; // JS SDK용 콜백 URL
const userProfile = ref(null);
//const STATE = "RANDOM_STRING"; // CSRF 방지용 난수 (랜덤으로 생성 권장)

function goNaverLogin() {
  const state = Math.random().toString(36).substring(2, 12);
  const url =
    `https://nid.naver.com/oauth2.0/authorize?response_type=code` +
    `&client_id=${NAVER_CLIENT_ID}` +
    `&redirect_uri=${encodeURIComponent(NAVER_CALLBACK_URL)}` +
    `&state=${state}`;

  window.location.href = url;
}

function loginWithKakao() {
  console.log("카카오 로그인 클릭")
  router.push('/menu')
}

</script>

<template>
  <NavMenu />
  <div class="login-container">
    <div class="login-card">
      <h2 class="login-title">간편 로그인</h2>

       <button class="login-btn naver-btn" @click="goNaverLogin">
        <img src="/img/naver_icon.png" alt="Naver" />
        네이버로 로그인
      </button>

      <!-- 비즈니스 인증 이슈로 제거
      <button class="login-btn kakao-btn" @click="loginWithKakao">
        <img src="https://developers.kakao.com/tool/resource/static/img/button/login/full/ko/kakao_login_small.png" alt="Kakao" />
        카카오로 로그인
      </button>
      -->
    </div>
  </div>
    <div v-if="userProfile">
      <h3>로그인 완료</h3>
      <p>이메일: {{ userProfile.email }}</p>
      <p>닉네임: {{ userProfile.nickname }}</p>
      <p>나이: {{ userProfile.age }}</p>
    </div>
</template>

<style scoped>

.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 64px);
  background-color: inherit;
}

.login-card {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 24px 32px;
  border-radius: 12px;
  background-color: rgba(255, 255, 255, 0.95);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  text-align: center;
  width: 400px;       
  max-width: 90%;
}

.login-title {
  margin-bottom: 8px;
  font-size: 20px;
  font-weight: 600;
  color: #333;
}

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

#naver_id_login {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.kakao-btn {
  background-color: #FEE500;
  color: #3C1E1E;
}

.kakao-btn:hover {
  filter: brightness(0.9);
}

.login-btn img {
  width: 30px;
  height: 30px;
}

#naver_id_login {
  margin-top: 20px;
}
</style>
