<script setup>
import { ref, watch, onMounted } from 'vue';
import { useDisplay } from 'vuetify';
import axios from 'axios';
import NavMenu2 from '@/components/NavMenu2.vue';
import MenuForm from '@/components/MenuForm.vue';

const { smAndDown } = useDisplay();

const selectedCenter = ref('');
const srchDt = ref(new Date().toISOString().slice(0, 10));

const centerList = ref([]);

const menuImage = ref('/img/no_mlmenu.png');
const menuLink = ref('');
const period = ref('');
const updated = ref('');
const currentIndex = ref(0);
const menuList = ref([]);

const currentInfoType = ref('');

// 등록/수정 폼 표시 여부
const showForm = ref(false);

// 수정할 메뉴 ID (null이면 신규 등록)
const editingMenuId = ref(null);

// 식당 리스트 조회
const fetchCenterList = async () => {
  try {
    const response = await axios.get('/api/rest/list', { params: { useYn: 'Y' } });
    centerList.value = response.data;

    if (centerList.value.length > 0) {
      const first = centerList.value[0];
      selectedCenter.value = first.restId;
      currentInfoType.value = first.infoInitType || '';
      await fetchMenuInfo(selectedCenter.value, currentInfoType.value);
    }
  } catch (error) {
    console.error('식당 리스트 조회 실패:', error);
  }
};

// 메뉴 정보 조회
const fetchMenuInfo = async (restId, infoType) => {
  try {
    const response = await axios.post('/api/mlmenu/rest', {
      restId,
      infoInitType: infoType,
      srchDt: srchDt.value
    });

    const menuData = response.data.MlMenuDto;
    const atchList = response.data.atchList || [];

    if (infoType === "DAY") {
      if (menuData) {
        period.value = `${menuData.bgngDt} ~ ${menuData.endDt}`;
        updated.value = menuData.mdfcnDt;
        menuLink.value = menuData.outsdReferUrl || '';
        menuImage.value = '';
        editingMenuId.value = menuData.mlMenuId || null; // ID 저장 (수정시 사용)
      } else {
        menuImage.value = '';
        menuLink.value = '등록된 식단이 존재하지 않습니다.';
        period.value = '';
        updated.value = '';
        editingMenuId.value = null;
      }
    } else {
      if (Array.isArray(menuData) && menuData.length > 0) {
        const currentIdx = menuData.findIndex(m => m.posType === 'CURRENT');
        currentIndex.value = currentIdx !== -1 ? currentIdx : 0;
        menuList.value = menuData;
        const currentMenu = menuList.value[currentIndex.value];
        period.value = `${currentMenu.bgngDt} ~ ${currentMenu.endDt}`;
        updated.value = currentMenu.mdfcnDt;

        if (atchList && atchList[0].filePath) {
          menuImage.value = `http://localhost:8080/atch/${atchList[0].filePath.split(/[/\\]/).at(-1)}`;
        } else {
          menuImage.value = '/img/no_mlmenu.png';
        }
        menuLink.value = '';
        editingMenuId.value = currentMenu.mlMenuId || null; // ID 저장
      } else {
        menuList.value = [];
        menuImage.value = '/img/no_mlmenu.png';
        menuLink.value = '';
        period.value = '';
        updated.value = '';
        editingMenuId.value = null;
      }
    }
  } catch (error) {
    console.error('메뉴 정보 조회 실패:', error);
    menuImage.value = '/img/no_mlmenu.png';
    menuLink.value = '';
    period.value = '';
    updated.value = '';
    editingMenuId.value = null;
  }
};

const goPrev = async () => {
  if (currentIndex.value > 0) {
    currentIndex.value--;
    srchDt.value = menuList.value[currentIndex.value].bgngDt;
    await fetchMenuInfo(selectedCenter.value, currentInfoType.value);
  }
};

const goNext = async () => {
  if (currentIndex.value < menuList.value.length - 1) {
    currentIndex.value++;
    srchDt.value = menuList.value[currentIndex.value].bgngDt;
    await fetchMenuInfo(selectedCenter.value, currentInfoType.value);
  }
};

onMounted(async () => {
  await fetchCenterList();
});

// 식당 변경 시 메뉴 재조회
watch(selectedCenter, async (newVal) => {
  if (newVal) {
    const selected = centerList.value.find(c => c.restId === newVal);
    currentInfoType.value = selected?.infoInitType || '';
    await fetchMenuInfo(newVal, currentInfoType.value);
  }
});

const openEditModal = async () => {
  if (currentInfoType.value !== 'DAY') {
    if (menuList.value.length > 0 && currentIndex.value >= 0) {
      editingMenuId.value = menuList.value[currentIndex.value].mlMenuId;
    } else {
      editingMenuId.value = null;
    }
  } else {
    if (!editingMenuId.value) {
      // 업로드 후 null 상태라면 API 재조회
      await fetchMenuInfo(selectedCenter.value, currentInfoType.value);
    }
  }
  showForm.value = true;
};

// 등록/수정 폼에서 완료 시 호출되는 이벤트 핸들러
const handleRegistered = async () => {
  showForm.value = false;
  editingMenuId.value = null; // 등록 완료 후 편집 상태 해제
  await fetchMenuInfo(selectedCenter.value, currentInfoType.value);
};

// 신규 등록 버튼 클릭 시
const onNewUploadClick = () => {
  editingMenuId.value = null; // 신규 등록
  showForm.value = true;
};

</script>

<template>
  <NavMenu2 />

  <v-main fluid class="main-wrapper">
    <div class="full-bleed-background">
      <div class="content-wrapper">
        <!-- 식당 선택 -->
        <div class="select-wrapper">
          <v-select
            v-model="selectedCenter"
            :items="centerList"
            item-title="restNm"
            item-value="restId"
            dense
            outlined
            hide-details
            style="max-width: 200px"
          />
        </div>

        <!-- 링크 또는 이미지 -->
        <div class="image-wrapper">
          <div v-if="menuLink">
            <p>
              식단 링크:
              <a
                :href="menuLink"
                target="_blank"
                style="color: blue; text-decoration: underline;"
              >
                {{ menuLink }}
              </a>
            </p>
          </div>

          <div v-else style="position: relative;">
            <v-icon
              v-if="menuImage && currentIndex > 0"
              class="arrow-left"
              @click="goPrev"
            >
              mdi-chevron-left
            </v-icon>

            <img :src="menuImage" alt="식단 이미지" class="menu-image" />

            <v-icon
              v-if="menuImage && currentIndex < menuList.length - 1"
              class="arrow-right"
              @click="goNext"
            >
              mdi-chevron-right
            </v-icon>
          </div>
        </div>

        <!-- 기간 정보 + 수정 버튼 -->
        <v-container class="period-container">
          <v-row align="center">
            <v-col cols="12" class="text-center">
              <v-text class="period-text font-weight-bold">
                해당기간 : {{ period }}
              </v-text>
              <v-text class="period-text">
                최근 업데이트 : {{ updated }}
              </v-text>
            </v-col>

            <v-btn
              variant="text"
              color="primary"
              class="edit-btn"
              @click="openEditModal"
            >
              수정
            </v-btn>
          </v-row>
        </v-container>

        <!-- 버튼 목록 -->
        <v-btn
          block
          class="my-1 func-btns"
          color="black"
          dark
          :density="smAndDown ? 'compact' : 'default'"
          @click="onNewUploadClick"
        >
          업로드 하기
        </v-btn>
        <v-btn
          block
          class="my-1 func-btns"
          color="black"
          dark
          :density="smAndDown ? 'compact' : 'default'"
          :to="'/rest/restInfo'"
        >
          식당정보 조회
        </v-btn>
        <v-btn
          block
          class="my-1 func-btns"
          color="black"
          dark
          :density="smAndDown ? 'compact' : 'default'"
        >
          삭제(관리자)
        </v-btn>
      </div>
    </div>
  </v-main>

  <!-- MenuForm 컴포넌트에 mlMenuId 전달 -->
  <MenuForm
    v-if="showForm"
    v-model="showForm"
    :ml-menu-id="editingMenuId"
    @registered="handleRegistered"
  />
</template>

<style scoped>
.main-wrapper {
  padding: 0;
  margin: 0;
}

.full-bleed-background {
  width: 100%;
  height: 100%;
  padding-top: 44px;
  overflow: hidden;
  box-sizing: border-box;
}

.content-wrapper {
  display: flex;
  flex-direction: column;
  height: 100%;
  box-sizing: border-box;
}

.select-wrapper {
  display: flex;
  justify-content: flex-end;
  height: 10px;
  flex-shrink: 0;
}

.image-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 60px;
}

.menu-image {
  max-width: 100%;
  height: auto;
  max-height: 700px;
  object-fit: contain;
}

.period-container {
  position: relative;
}

.period-text {
  display: block;
  text-align: center;
  font-size: 16px;
  font-weight: 500;
  color: #333;
  line-height: 1.6;
}

.edit-btn {
  position: absolute;
  top: 20px;
  right: 5px;
  font-size: 14px;
  min-width: 50px;
  padding: 0 8px;
  transition: background-color 0.2s, color 0.2s;
}

.edit-btn:hover {
  background-color: rgba(33, 150, 243, 0.1);
}

.func-btns {
  font-size: 16px;
  height: 50px;
  max-height: 50px;
  line-height: 50px;
  white-space: nowrap;
}

.arrow-left,
.arrow-right {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  z-index: 10;
  cursor: pointer;
  font-size: 36px;
  width: 48px;
  height: 48px;
  line-height: 48px;
  border-radius: 50%;
  text-align: center;
  background-color: rgba(0, 0, 0, 0.4);
  color: white;
  transition: background-color 0.3s;
}

.arrow-left:hover,
.arrow-right:hover {
  background-color: rgba(0, 0, 0, 0.6);
}

.arrow-left {
  left: 10px;
}

.arrow-right {
  right: 10px;
}
</style>
