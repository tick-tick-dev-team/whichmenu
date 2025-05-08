// 문의게시판 : 일반 게시판으로 분리되어 BBS_TYPE = P
<script setup>
import PostForm from '@/components/PostForm.vue';
import PostList from '@/components/PostList.vue';
import CmntList from '@/components/CmntList.vue';
import NavMenu2 from '@/components/NavMenu2.vue';
import axios from 'axios';
import { onMounted, ref } from 'vue';

// 리스트
const searchKeyword = ref('');
const restInfo = ref([
  {
    restId: null,
    restNm: "서버를 켰는지 확인해주세요 이거 나오면 안 켜졌을수도 있음...!",
    restDtl: "서버 장애",
    restAddr: "-",
    useYn: "-",
    infoInitType : "-",
    regDt: "2025-04-18 17:26:18",
    mdfcnDt: "2025-04-18 17:26:18",
  }
]);

// 조회 게시글 리스트 가져오는 함수
// axios로 동기처럼 처리
function restList() {
  axios.get('/api/rest/list', {
    //params: { useYn: 'Y' }
  })
  .then(response => {
    restInfo.value = response.data;  // 응답 데이터를 inquiries에 할당
    console.log(restInfo.value);
  })
  .catch(error => {
    console.error('문의 게시글 목록 가져오기 실패:', error);
  });
}

// 수정 버튼 클릭 처리
function handleEdit(restId) {
    console.log('수정 클릭된 식당 ID:', restId);
    // 수정 로직 추가
}

// 삭제 버튼 클릭 처리
function handleDelete(restId) {
    console.log('삭제 클릭된 식당 ID:', restId);
    // 삭제 로직 추가
}

onMounted(() => {
  restList();
})
</script>

<template>
  <!-- 헤더 -->
  <NavMenu2 />

  <div class="bbs-container">
      <h2 class="page-title">식당관리 - 관리자</h2>

      <!-- 검색창 -->
      <v-text-field
          v-model="searchKeyword"
          label="검색어를 입력하세요"
          prepend-inner-icon="mdi-magnify"
          clearable
          variant="outlined"
          color="primary"
          class="mb-4 search-field"
      />

      <v-row dense>
          <v-col v-for="rest in restInfo" :key="rest.restId" cols="12">
              <v-card class="post-card" outlined>
                  <v-card-text>
                      <div class="rest-header">
                          <h3 class="rest-name">{{ rest.restNm }}</h3>
                          <v-chip
                              v-if="rest.infoInitType === 'WEEK' || rest.infoInitType === 'DAY'"
                              color="deep-purple-accent-2"
                              text-color="white"
                              size="small"
                              class="info-chip"
                              label
                              rounded
                          >
                          {{ rest.infoInitType === 'WEEK' ? '주간' : '일일' }}
                          </v-chip>
                      </div>
                      <div class="post-meta">{{ rest.mdfcnDt }}</div>
                      <p class="post-detail">📍 {{ rest.restAddr }}</p>
                      <p class="post-detail">📝 {{ rest.restDtl }}</p>


                      <div class="post-actions">
                          <v-btn size="x-small" variant="text" @click="emit('editComment', post.bbsId)">수정</v-btn>
                          <v-btn size="x-small" variant="text" color="error" @click="emit('deleteComment', post.bbsId)">삭제</v-btn>
                      </div>
                  </v-card-text>
              </v-card>
          </v-col>
      </v-row>

      <!-- + 버튼 -->
      <v-btn class="floating-btn" color="deep-purple-accent-2" fab @click="addPost">
          <v-icon>mdi-plus</v-icon>
      </v-btn>
  </div>
</template>

<style scoped>
.bbs-container {
  min-height: 100vh;
  padding: 20px;
  margin-top: 30px;
  position: relative;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.page-title {
  font-size: 1.6rem;
  font-weight: 600;
  margin-bottom: 20px;
  text-align: center;
  color: #333;
}

.search-field {
  width: 100%;
}

.post-card {
  margin-bottom: 20px;
  padding: 16px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  background-color: #fff;
}

.rest-name {
  font-size: 1.3rem;
  font-weight: bold;
  color: #3f51b5;
  margin-bottom: 12px;
  text-align: center;
}

.post-detail {
  font-size: 0.95rem;
  margin-bottom: 6px;
  color: #444;
  text-align: left;
  padding-left: 4px;
}

.post-meta {
  font-size: 0.8rem;
  color: #999;
  text-align: right;
  margin-top: 10px;
  margin-bottom: 8px;
}

.post-actions {
  display: flex;
  justify-content: flex-end;
  gap: 6px;
}

.floating-btn {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 1000;
  width: 56px;
  height: 56px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 26px;
}

.rest-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 8px;
}

.info-chip {
    font-weight: 600;
}
</style>
