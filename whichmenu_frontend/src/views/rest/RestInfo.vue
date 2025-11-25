// 문의게시판 : 일반 게시판으로 분리되어 BBS_TYPE = P
<script setup>
import NavMenu from '@/components/NavMenu.vue';
import RestForm from '@/components/RestForm.vue';
import axios from 'axios';
import { useRoute } from 'vue-router'
import { onMounted, ref, watch } from 'vue';
import { useUserStore } from '@/stores/userStore';

const route = useRoute();
const userStore = useUserStore();
// 리스트
const searchKeyword = ref(route.query.keyword || '');
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
        params: { 
          useYn    : 'Y', 
          srchKwrd : searchKeyword.value
         }
    })
    .then(response => {
      restInfo.value = [];
      restInfo.value = response.data;  // 응답 데이터를 inquiries에 할당
    })
    .catch(error => {
      console.error('문의 게시글 목록 가져오기 실패:', error);
    });
}

const addRestVisible = ref(false);

// 자식 컴포넌트에서 등록 완료 시 호출되는 함수
function handleRegistered() {
  restList();       // 리스트 갱신
  addRestVisible.value = false;  // 다이얼로그 닫기 (이미 자식에서 닫았다면 생략 가능)
  editTarget.value = null;
}


onMounted(() => {
  restList();
})

// 삭제 버튼 클릭 시 처리
async function deleteRest(restInfo) {
    try {
        // 유효성 체크 (예: 식당명, 식당주소 필수)
        if (!restInfo.restId) {
            alert('식당 ID가 존재하지 않습니다.')
            return
        }

        if (!confirm('정말 삭제하시겠습니까?')) {
            return; // 취소한 경우 삭제하지 않음
        }

        try {
            const res = await axios.post('/api/rest/delete', restInfo , {
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (res.data.result === 'success') {
                alert('식당이 삭제 되었습니다.');
                handleRegistered();
            } else {
                alert('삭제 실패: ' + res.data.message);
            }

            
        } catch (e) {
            console.error(e);
            alert('에러 발생');
        }
    }    
    catch (error) {
        console.error(error)
        alert('서버 오류가 발생했습니다.')
    }
}

const editTarget = ref(null); // 수정할 데이터를 저장

function editRest(restId) {
    const target = restInfo.value.find(r => r.restId === restId);
    if (target) {
        editTarget.value = { ...target }; // 복사본 전달 (mutable 대응)
        addRestVisible.value = true;
    }
}

watch(searchKeyword, () => {
  restList();
});


</script>

<template>
  <!-- 헤더 -->
  <NavMenu />

  <div class="bbs-container">
      <h2 class="page-title">식당조회</h2>

      <!-- 검색창 -->
      <v-text-field
          v-model="searchKeyword"
          label="검색어를 입력하세요"
          prepend-inner-icon="mdi-magnify"
          clearable
          variant="outlined"
          color="primary"
          class="mb-4 search-field"
          full-width
      />

      <v-row dense>
          <v-col v-for="rest in restInfo" :key="rest.restId" cols="12" style="min-width: 0;">
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
                          <v-btn v-if="userStore.isLoggedIn && userStore.user?.usrRole === 'A'" size="x-small" variant="text" @click="editRest(rest.restId)">수정</v-btn>
                          <v-btn v-if="userStore.isLoggedIn && userStore.user?.usrRole === 'A'" size="x-small" variant="text" color="error" @click="deleteRest(rest)">삭제</v-btn>
                      </div>
                  </v-card-text>
              </v-card>
          </v-col>
      </v-row>

      <!-- + 버튼 -->
      <v-btn class="floating-btn" color="deep-purple-accent-2" fab @click="addRestVisible = true">
          <v-icon size="30">mdi-plus</v-icon>
      </v-btn>

      <!-- 등록 다이얼로그 연결 -->
      <RestForm :model-value="addRestVisible" @update:modelValue="val => addRestVisible = val" @registered="handleRegistered" :edit-data="editTarget"/>
  </div>
</template>

<style scoped>
.bbs-container {
  min-height: 100vh;
  padding: 20px;
  margin-top: 30px;
  position: relative;
  max-width: 600px;
  width: 100%;
  min-width: 0; /* flex shrink 방지 */
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
  min-width: 0;
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
  width: 50px;
  height: 50px;
  min-width: 50px;
  min-height: 50px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0;
  font-size: 24px; /* 또는 아래 방법 사용 */
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
