// 추천 게시판
<script setup>
import PostForm from '@/components/PostForm.vue';
import PostList from '@/components/PostList.vue';
import CmntList from '@/components/CmntList.vue';
import NavMenu from '@/components/NavMenu.vue';
import { ref } from 'vue';

const posts = ref([
  {
    id: 1,
    content: '맛집 추천합니다!',
    author: '홍길동',
    createdAt: '2025-03-05',
    comments: [{ id: 1, content: '여기 진짜 맛있어요!', author: '김철수', createdAt: '2025-03-05' }]
  }
]);

const addComment = (postId, comment) => {
  const post = posts.value.find(p => p.id === postId);
  post.comments.push({
    id: new Date().getTime(),
    content: comment,
    author: '익명',
    createdAt: new Date().toISOString().split('T')[0]
  });
};
</script>

<template>
  <div class="bbs-container">
    <NavMenu /> <!-- 햄버거 메뉴 추가 -->

    <div class="board">
      <h1>맛집을 알려주세요</h1>
      <PostForm @submitPost="(content) => posts.push({ id: new Date().getTime(), content, author: '익명', createdAt: new Date().toISOString().split('T')[0], comments: [] })" />
      
      <div v-for="post in posts" :key="post.id">
        <PostList :post="post" />
        <CmntList :comments="post.comments" boardType="restaurant" @addComment="(comment) => addComment(post.id, comment)" />
      </div>
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