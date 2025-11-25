<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/userStore';
import { useDisplay } from 'vuetify';

const { smAndDown } = useDisplay();
const isOpen = ref(false);
const router = useRouter();
const userStore = useUserStore();

const navigate = (path) => {
    isOpen.value = false;
    router.push(path);
};

const logout = async () => {
  try {
    // 1️⃣ 백엔드 세션 로그아웃 호출
    await fetch("/api/oauth/logout", {
      method: "POST",
      credentials: "include" // 🔥 중요: 세션 유지 (쿠키 포함)
    });

    // 2️⃣ 프론트 상태 초기화 (pinia)
    userStore.logout();

    // 3️⃣ 로그인 페이지로 이동
    router.push('/login');

  } catch (err) {
    console.error("로그아웃 요청 실패:", err);
  }
};
</script>
<template>
<!-- 헤더 -->
<v-app-bar
app
dense
flat
color="grey-darken-4"
class="px-4"
>
<div class="d-flex align-center nav-title" @click="navigate('/')">
    <v-icon class="mr-2" color="white">mdi-silverware-fork-knife</v-icon>
    <span class="text-h6 font-weight-bold text-white">WHICH MENU</span>
</div>
<v-spacer />

  <!-- 오른쪽: 로그인 메시지 -->
  <span 
    class="text-h6 text-white mr-4"
    v-if="userStore.isLoggedIn && !smAndDown"
  >
    {{ userStore.user?.nickNm }}님 환영합니다 👋
  </span>

  <!-- 오른쪽: 햄버거 메뉴 -->
  <v-menu
    v-model="isOpen"
    offset-y
    transition="scale-transition"
    location="bottom right"
  >
    <template #activator="{ props }">
      <v-btn icon v-bind="props">
        <v-icon>mdi-menu</v-icon>
      </v-btn>
    </template>

    <v-list>
      <v-list-item v-if="userStore.isLoggedIn">
        {{ userStore.user?.nickNm }}님 환영합니다 👋
      </v-list-item>

      <v-list-item @click="navigate('/')">
        <v-list-item-title>식단</v-list-item-title>
      </v-list-item>
      <v-list-item @click="navigate('/bbs/notice')">
        <v-list-item-title>공지</v-list-item-title>
      </v-list-item>
      <v-list-item @click="navigate('/bbs/rcmnd')">
        <v-list-item-title>맛집을 알려주세요!</v-list-item-title>
      </v-list-item>
      <v-list-item @click="navigate('/bbs/inquiry')">
        <v-list-item-title>문의게시판</v-list-item-title>
      </v-list-item>
      <v-list-item @click="navigate('/rest/restInfo')">
        <v-list-item-title>식당관리</v-list-item-title>
      </v-list-item>
      <v-list-item @click="navigate('/rest/logList')">
        <v-list-item-title>로그확인</v-list-item-title>
      </v-list-item>

      <v-list-item v-if="!userStore.isLoggedIn" @click="navigate('/login')">
        <v-list-item-title>로그인</v-list-item-title>
      </v-list-item>

      <v-list-item v-else @click="logout">
        <v-list-item-title>로그아웃</v-list-item-title>
      </v-list-item>
    </v-list>
  </v-menu>
</v-app-bar>
</template>

<style scoped>
.nav-wrapper {
    display: flex;
    align-items: center;
}

.nav-title {
  cursor: pointer;
  transition: opacity 0.2s;
}

.nav-title:hover {
  opacity: 0.8;
}
</style>