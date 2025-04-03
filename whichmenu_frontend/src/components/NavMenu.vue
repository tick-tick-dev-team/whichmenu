<!-- NavMenu.vue -->
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
  <div class="nav-wrapper">
    <button class="hamburger" @click="isOpen = !isOpen">☰</button>
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
.nav-wrapper {
  position: relative; /* 기준이 되는 부모 */
  display: flex;
  justify-content: flex-end;
  width: 100%;
}

/* 햄버거 버튼 */
.hamburger {
  font-size: 24px;
  background: none;
  border: none;
  cursor: pointer;
  z-index: 10;
}

/* 메뉴: 버튼 아래에 착 붙기 */
.nav-menu {
  position: absolute;
  top: 100%; /* 버튼 바로 아래 */
  right: 0;
  background-color: #f5ecf5;
  padding: 3px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 5;
  width: max-content;
}

.nav-menu ul {
  list-style: none;
  margin: 0;
  padding: 0;
}

.nav-menu li {
  padding: 8px 12px;
  cursor: pointer;
  white-space: nowrap;
}

.nav-menu li:hover {
  background-color: #e0d7e0;
}

</style>
