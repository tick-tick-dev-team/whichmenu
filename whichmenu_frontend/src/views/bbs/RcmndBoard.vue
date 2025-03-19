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
    <!-- 전체 레이아웃을 감싸는 flex 컨테이너 -->
    <div class="layout-container">
      <!-- 좌측: 네비게이션 (햄버거 메뉴) -->
      <div class="nav-container">
        <NavMenu />
      </div>

      <!-- 우측: 게시판 컨텐츠 -->
      <div class="board">
        <h1>맛집을 알려주세요</h1>
        <PostForm @submitPost="(content) => posts.push({ id: new Date().getTime(), content, author: '익명', createdAt: new Date().toISOString().split('T')[0], comments: [] })" />
        
        <div v-for="post in posts" :key="post.id" class="post-list">
          <PostList :post="post" />
          <CmntList :comments="post.comments" boardType="restaurant" @addComment="(comment) => addComment(post.id, comment)" />
        </div>
      </div>
    </div>
  </div>
</template>


<style scoped>
/* 전체 컨테이너 스타일 */
.bbs-container {
  background-color: #ffffff;
  min-height: 100vh;
  padding: 16px;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* 네비게이션과 게시판을 감싸는 레이아웃 컨테이너 */
.layout-container {
  display: flex;
  width: 100%;
  max-width: 900px;
  align-items: flex-start;
}

/* 네비게이션 메뉴 (맨 위 배치) */
.nav-menu {
  align-self: flex-start;
  margin-bottom: 8px;
}

/* 로고 라벨 (제목) */
h1 {
  font-size: 1.8rem;
  font-weight: bold;
  text-align: center;
  margin-bottom: 16px;
}

/* 게시판 박스 */
.board {
  background-color: white;
  padding: 16px;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 600px;
}

/* 입력 필드 스타일 */
.v-text-field {
  width: 100%;
  border: 1px solid #ccc !important;
  border-radius: 8px;
  padding: 10px;
}

/* 버튼 스타일 */
.v-btn {
  width: 100%;
  margin-top: 10px;
  text-transform: none;
  font-size: 1rem;
  font-weight: 600;
  border: 1px solid #aaa !important;
  border-radius: 8px;
}

/* 댓글 리스트 스타일 */
.cmnt-list {
  margin-top: 12px;
  padding: 12px;
  background-color: #f9f9f9;
  border-radius: 8px;
}

/* 등록된 글 스타일 (좌측 정렬) */
.post-list {
  width: 100%;
  text-align: left;
  padding: 10px 0;
}

/* 댓글 항목 */
.cmnt-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px;
  border-bottom: 1px solid #ddd;
}

/* 아이콘 버튼 스타일 */
.icon-btn {
  cursor: pointer;
  font-size: 18px;
  margin-left: 8px;
}

/* 이미지 스타일 */
img {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  margin-top: 8px;
}

/* 반응형 (모바일 최적화) */
@media screen and (max-width: 600px) {
  h1 {
    font-size: 1.5rem;
  }

  .board {
    padding: 12px;
  }

  .v-btn {
    font-size: 0.9rem;
  }
}
</style>