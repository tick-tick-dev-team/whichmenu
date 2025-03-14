// 문의게시판
<script setup>
import PostForm from '@/components/PostForm.vue';
import FileUpload from '@/components/FileUpload.vue';
import PostList from '@/components/PostList.vue';
import CmntList from '@/components/CmntList.vue';
import NavMenu from '@/components/NavMenu.vue';
import { ref } from 'vue';

const inquiries = ref([
  {
    id: 1,
    content: '문의드립니다.',
    author: '사용자A',
    createdAt: '2025-03-05',
    comments: []
  }
]);

const addComment = (postId, comment) => {
  const post = inquiries.value.find(p => p.id === postId);
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
    
    <h1>문의게시판</h1>
    <PostForm @submitPost="(content) => inquiries.push({ id: new Date().getTime(), content, author: '익명', createdAt: new Date().toISOString().split('T')[0], comments: [] })" />
	<FileUpload></FileUpload>
    <div v-for="inquiry in inquiries" :key="inquiry.id">
      <PostList :post="inquiry" />
      <CmntList :comments="inquiry.comments" boardType="inquiry" @addComment="(comment) => addComment(inquiry.id, comment)" />
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