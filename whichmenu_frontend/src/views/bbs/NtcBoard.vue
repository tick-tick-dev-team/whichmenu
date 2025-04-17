// 공지 게시판
<script setup>
import PostForm from '@/components/PostForm.vue';
import PostList from '@/components/PostList.vue';
import NavMenu2 from '@/components/NavMenu2.vue';
import axios from 'axios';
import { onMounted, ref } from 'vue';

const notices = ref([
  { 
    atchReferId: null,
    bbsCn: "서버를 켰는지 확인해주세요 이거 나오면 안 켜졌을수도 있음...!",
    bbsId: "9",
    bbsTtl: "공지사항",
    bbsType: "N",
    bbscttPw: null,
    mdfcnDt: "2025-04-07 14:46:30",
    regDt: "2025-04-07 14:46:30",
    regNm: "ADMIN",
    rlsYn: "Y",
    useYn: "Y",
   }
]);

// 조회 게시글 리스트 가져오는 함수
async function bbsList() {
  axios.get('/api/bbs/list', {
    params: { bbsType: 'N' }
  })
  .then(response => {
    notices.value = response.data;  // 응답 데이터를 notices에 할당
    console.log(notices.value);
  })
  .catch(error => {
    console.error('공지 게시글 목록 가져오기 실패:', error);
  });
}
onMounted(() => {
  bbsList();
})
</script>

<template>
  <div class="bbs-container">
    <!-- 헤더 -->
    <NavMenu2 />
    
    <!-- 로고 텍스트 -->
    <h2>공지</h2>
    
    <!--<PostForm @submitPost="(content) => notices.push({ id: new Date().getTime(), content, author: '관리자', createdAt: new Date().toISOString().split('T')[0] })" />
      -->
    
    <div v-for="notice in notices" :key="notice.id">
      <PostList :post="notice" />
    </div>
    <!-- + 버튼 추가 -->
    <v-btn class="floating-btn" color="deep-purple-accent-2" fab @click="addPost">
        <v-icon>mdi-plus</v-icon>
    </v-btn>
  </div>
</template>

<style scoped>
.bbs-container {
  background-color: #ffffff; /* 메뉴 배경색 */
  min-height: 100vh;
  padding: 20px;
  margin-top: 30px;
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
.post-list {
  margin-top : 20px;
}

/* 우측 하단에 + 버튼을 고정 */
.floating-btn {
    position: fixed;
    bottom: 20px;
    right: 20px;
    z-index: 1000;
    width: 50px; /* 원 크기 */
    height: 50px; /* 원 크기 */
    border-radius: 50%; /* 동그라미 모양 */
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 0;
    min-width: 50px; /* 버튼의 최소 너비 */
    min-height: 50px; /* 버튼의 최소 높이 */
    font-size: 24px; /* 아이콘 크기 */
}
</style>