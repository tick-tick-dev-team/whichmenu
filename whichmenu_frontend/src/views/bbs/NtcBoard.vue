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
    <NavMenu />
    
    <!-- 로고 텍스트 -->
    <h2>공지</h2>
    
    <PostForm @submitPost="(content) => notices.push({ id: new Date().getTime(), content, author: '관리자', createdAt: new Date().toISOString().split('T')[0] })" />
    
    <div v-for="notice in notices" :key="notice.id">
      <PostList :post="notice" />
    </div>
  </div>
</template>

<style scoped>
.bbs-container {
  background-color: #ffffff; /* 메뉴 배경색 */
  min-height: 100vh;
  padding: 20px;
  position: relative;
}

.board {
  background-color: white; /* 게시판 내용은 흰색 */
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  max-width: 800px;
  margin: auto;
}
</style>