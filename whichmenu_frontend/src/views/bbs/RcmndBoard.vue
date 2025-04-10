// 추천 게시판
<script setup>
import PostForm from '@/components/PostForm.vue';
import PostList from '@/components/PostList.vue';
import CmntList from '@/components/CmntList.vue';
import NavMenu from '@/components/NavMenu.vue';
import axios from 'axios';
import { onMounted, ref } from 'vue';

const posts = ref([
{
    atchReferId: null,
    bbsCn: "서버를 켰는지 확인해주세요 이거 나오면 안 켜졌을수도 있음...!",
    bbsId: "10",
    bbsTtl: "관리자 문의",
    bbsType: "R",
    bbscttPw: "1234",
    mdfcnDt: "2025-04-07 17:26:18",
    regDt: "2025-04-07 17:26:18",
    regNm: "USER",
    rlsYn: "Y",
    useYn: "Y",
  }
]);

// 조회 게시글 리스트 가져오는 함수
async function bbsList() {
  try {
    const response = await axios.get('/bbs/list', {
      params: {
        bbsType: 'R'
      }
    })
    posts.value = response.data
  } catch (error) {
    console.error('추천 게시글 목록 가져오기 실패:', error)
  }
}

const addComment = (postId, comment) => {
  const post = posts.value.find(p => p.id === postId);
  post.comments.push({
    id: new Date().getTime(),
    content: comment,
    author: '익명',
    createdAt: new Date().toISOString().split('T')[0]
  });
};
onMounted(() => {
  bbsList();
})
</script>

<template>
  <div class="bbs-container">
    <NavMenu />

    <h2>맛집을 알려주세요</h2>
    <PostForm @submitPost="(content) => posts.push({ id: new Date().getTime(), content, author: '익명', createdAt: new Date().toISOString().split('T')[0], comments: [] })" />
    
    <div v-for="post in posts" :key="post.id" class="post-list">
      <PostList :post="post" />
      <CmntList :comments="post.comments" boardType="restaurant" @addComment="(comment) => addComment(post.id, comment)" />
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