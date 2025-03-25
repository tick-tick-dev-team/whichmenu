// 공지 게시판
<script setup>
import PostForm from '@/components/PostForm.vue';
import PostList from '@/components/PostList.vue';
import NavMenu from '@/components/NavMenu.vue';
import { ref } from 'vue';

const notices = ref([
  { id: 1, content: '새로운 공지사항입니다.', author: '관리자', createdAt: '2025-03-05' }
]);
</script>

<template>
  <div class="bbs-container">
    <!-- 햄버거 메뉴 -->
    <div class="menu-row">
      <NavMenu />
    </div> 
    <!-- 로고 텍스트 -->
    <div class="logo-row">
      <h1>공지</h1>
    </div>
    
    <PostForm @submitPost="(content) => notices.push({ id: new Date().getTime(), content, author: '관리자', createdAt: new Date().toISOString().split('T')[0] })" />
    
    <div v-for="notice in notices" :key="notice.id">
      <PostList :post="notice" />
    </div>
  </div>
</template>

<style scoped>
.bbs-container {
  background-color: #ffffff;
  min-height: 100vh;
  padding: 16px;
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: 600px;
  margin: 0 auto;
}

/* 로고 줄 */
.logo-row {
  width: 100%;
  margin-bottom: 8px;
}

.logo-row h1 {
  font-size: 1.8rem;
  font-weight: bold;
  margin: 0;
  text-align: left; /* 좌측 정렬 */
}

/* 메뉴 줄 */
.menu-row {
  position: relative; /* ✅ 이거 꼭 있어야 함! */
  width: 100%;
  display: flex;
  justify-content: flex-end; /* 우측 정렬 */
  margin-bottom: 16px;
}

/* 게시판 컨텐츠 */
.board {
  background-color: white;
  padding: 16px;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 100%;
}
</style>