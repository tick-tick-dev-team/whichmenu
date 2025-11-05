<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import NavMenu from '@/components/NavMenu.vue';

const router = useRouter()
const NAVER_CLIENT_ID    = import.meta.env.VITE_NAVER_CLIENT_ID;
const NAVER_CALLBACK_URL = import.meta.env.VITE_NAVER_CALLBACK_URL; // JS SDK용 콜백 URL
const userProfile = ref(null);
//const STATE = "RANDOM_STRING"; // CSRF 방지용 난수 (랜덤으로 생성 권장)

function loginWithNaver() {
  if (!window.naver_id_login) {
    console.error("네이버 SDK가 로드되지 않았습니다.");
    return;
  }

  const naverLogin = new window.naver_id_login(NAVER_CLIENT_ID, NAVER_CALLBACK_URL);

  // naverLogin.setButton("green", 3, 48);
  const state = naverLogin.getUniqState(); // CSRF 방지를 위한 STATE 값 설정
  naverLogin.setState(state);

  naverLogin.setPopup(); // 팝업 로그인

 // naverLogin.init_naver_id_login();

  // JS SDK용 글로벌 콜백
  window.naverSignInCallback = function () {
    const accessToken = naverLogin.oauthParams.access_token; // 추가
    console.log("Access Token:", naverLogin.oauthParams.access_token);

    fetch("http://localhost:8080/api/auth/naver/token", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ access_token: accessToken }),
    })
    .then(res => res.json())
    .then(data => {
      console.log("서버에서 받은 로그인 유저:", data);
      
    })
    .catch(err => console.error("토큰 전송 실패:", err));
    // naverLogin.get_naver_userprofile(function () {
    //   const profile = {
    //     email: naverLogin.getProfileData('email'),
    //     nickname: naverLogin.getProfileData('nickname'),
    //     age: naverLogin.getProfileData('age')
    //   };
    //   console.log("Naver Profile:", profile);
    //   userProfile.value = profile;

    //   // 로그인 성공 후 원하는 페이지로 이동
    //   router.push('/ml/mlMain');
    // });
  };
}

function loginWithKakao() {
  console.log("카카오 로그인 클릭")
  router.push('/menu')
}

// mounted 시 네이버 SDK 동적 로드
onMounted(() => {
  const script = document.createElement("script");
  script.src = "https://static.nid.naver.com/js/naverLogin_implicit-1.0.3.js";
  script.charset = "utf-8";
  script.onload = () => {
    console.log("Naver SDK loaded");

    // ✅ SDK 버튼을 내가 둔 위치(#naver_id_login)에 생성
    const naverLogin = new window.naver_id_login(NAVER_CLIENT_ID, NAVER_CALLBACK_URL);
    const state = naverLogin.getUniqState();
    naverLogin.setState(state);

    // 버튼 스타일: green, 3(size), 48(height)
    naverLogin.setButton("green", 3, 48);
    //naverLogin.setPopup();
    naverLogin.init_naver_id_login();
  };
  document.head.appendChild(script);
});

</script>

<template>
  <NavMenu />
  <div class="login-container">
    <div class="login-card">
      <h2 class="login-title">간편 로그인</h2>

      <!-- <button class="login-btn naver-btn" @click="loginWithNaver">
        <img src="/img/naver_icon.png" alt="Naver" />
        네이버로 로그인
      </button> -->
       <div id="naver_id_login"></div>

      <button class="login-btn kakao-btn" @click="loginWithKakao">
        <img src="https://developers.kakao.com/tool/resource/static/img/button/login/full/ko/kakao_login_small.png" alt="Kakao" />
        카카오로 로그인
      </button>
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
