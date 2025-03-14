<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const isOpen = ref(false);
const router = useRouter();

const navigate = (path) => {
  isOpen.value = false;
  router.push(path);
};
</script>

<template>
  <div>
    <!-- 햄버거 버튼 -->
    <button class="hamburger" @click="isOpen = !isOpen">☰</button>

    <!-- 네비게이션 메뉴 -->
    <div v-if="isOpen" class="nav-menu">
      <ul>
        <li @click="navigate('/')">식단</li>
        <li @click="navigate('/bbs/notice')">공지</li>
        <li @click="navigate('/bbs/rcmnd')">맛집을 알려주세요!</li>
        <li @click="navigate('/bbs/inquiry')">문의게시판</li>
      </ul>
    </div>
  </div>
</template>

<style scoped>
.hamburger {
  position: absolute;
  top: 10px;
  right: 10px; /* 🚀 오른쪽 상단으로 이동 */
  font-size: 24px;
  background: none;
  border: none;
  cursor: pointer;
  z-index: 1000; /* 🚀 다른 요소 위에 표시 */
}

.nav-menu {
  position: absolute;
  top: 50px;
  right: 10px;
  background-color: #f5ecf5;
  padding: 10px;
  border-radius: 5px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  z-index: 999; /* 🚀 햄버거 버튼보다 낮은 값 */
}

.nav-menu ul {
  list-style: none;
  padding: 0;
}

.nav-menu li {
  padding: 10px;
  cursor: pointer;
}

.nav-menu li:hover {
  background-color: #ddd;
}
</style>

