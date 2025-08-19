<script setup>
import { ref, watch, onMounted } from 'vue';
import { useDisplay } from 'vuetify';
import axios from 'axios';
import NavMenu from '@/components/NavMenu.vue';
import MenuForm from '@/components/MenuForm.vue';

const { smAndDown } = useDisplay();

const selectedCenter = ref('');
const srchDt = ref(new Date().toISOString().slice(0, 10));

const centerList = ref([]);

const menuImage = ref('/img/no_mlmenu.png');
const menuLink = ref('');
const period = ref('');
const updated = ref('');
const menuList = ref([]);
const isMenuAvailable = ref(false); // 현재 식단 존재 여부 상태

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
  // 상태 초기화
  menuList.value = [];
  isMenuAvailable.value = false;
  editingMenuId.value = null;

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
        isMenuAvailable.value = true;
        period.value = `${menuData.bgngDt} ~ ${menuData.endDt}`;
        updated.value = menuData.mdfcnDt;
        menuLink.value = menuData.outsdReferUrl || '';
        menuImage.value = '';
        editingMenuId.value = menuData.mlMenuId || null;
      } else {
        isMenuAvailable.value = false;
        menuImage.value = '';
        menuLink.value = '등록된 식단이 존재하지 않습니다.';
        period.value = '';
        updated.value = '';
        editingMenuId.value = null;
      }
    } else { // WEEK 타입 (이미지)
      if (Array.isArray(menuData) && menuData.length > 0) {
        menuList.value = menuData; // 이전, 현재, 다음 목록 전체를 저장

        // 1. 현재 날짜에 해당하는 식단(CURRENT)을 찾음
        const currentMenu = menuList.value.find(m => m.posType === 'CURRENT');

        if (currentMenu) {
          // 2. CURRENT 식단이 있으면, 해당 정보를 표시
          isMenuAvailable.value = true;
          period.value = `${currentMenu.bgngDt} ~ ${currentMenu.endDt}`;
          updated.value = currentMenu.mdfcnDt;
          editingMenuId.value = currentMenu.mlMenuId || null;

          if (atchList && atchList[0]?.filePath) {
            menuImage.value = `http://localhost:8080/atch/${atchList[0].filePath.split(/[/\\]/).at(-1)}`;
          } else {
            menuImage.value = '/img/no_mlmenu.png';
          }
          menuLink.value = '';
        } else {
          // 3. CURRENT 식단이 없으면, '없음' 상태로 설정
          isMenuAvailable.value = false;
          menuImage.value = '/img/no_mlmenu.png';
          period.value = '해당 날짜에 등록된 식단이 없습니다.';
          updated.value = '';
          menuLink.value = '';
          editingMenuId.value = null;
        }
      } else {
        // 4. 서버에서 아무런 메뉴 데이터도 받지 못한 경우
        isMenuAvailable.value = false;
        menuImage.value = '/img/no_mlmenu.png';
        period.value = '해당 날짜에 등록된 식단이 없습니다.';
        updated.value = '';
        menuLink.value = '';
        editingMenuId.value = null;
      }
    }
  } catch (error) {
    console.error('메뉴 정보 조회 실패:', error);
    isMenuAvailable.value = false;
    menuList.value = [];
    menuImage.value = '/img/no_mlmenu.png';
    period.value = '메뉴를 불러오는데 실패했습니다.';
    updated.value = '';
    menuLink.value = '';
    editingMenuId.value = null;
  }
};

// 이전 식단으로 이동
const goPrev = async () => {
  const prevMenu = menuList.value.find(m => m.posType === 'PREV');
  if (prevMenu) {
    srchDt.value = prevMenu.bgngDt;
    await fetchMenuInfo(selectedCenter.value, currentInfoType.value);
  }
};

// 다음 식단으로 이동
const goNext = async () => {
  const nextMenu = menuList.value.find(m => m.posType === 'NEXT');
  if (nextMenu) {
    srchDt.value = nextMenu.bgngDt;
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
    // 식당 변경 시 검색 날짜를 오늘로 초기화
    srchDt.value = new Date().toISOString().slice(0, 10);
    await fetchMenuInfo(newVal, currentInfoType.value);
  }
});

const openEditModal = async () => {
  if (isMenuAvailable.value && editingMenuId.value) {
    showForm.value = true;
  }
};

// 등록/수정 폼에서 완료 시 호출되는 이벤트 핸들러
const handleRegistered = async () => {
  showForm.value = false;
  editingMenuId.value = null;
  await fetchMenuInfo(selectedCenter.value, currentInfoType.value);
};

// 신규 등록 버튼 클릭 시
const onNewUploadClick = () => {
  editingMenuId.value = null;
  showForm.value = true;
};
</script>

<template>
  <NavMenu />

  <v-main fluid class="main-wrapper">
    <div class="full-bleed-background">
      <div class="content-wrapper">
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

        <div class="image-wrapper">
          <div v-if="menuLink">
            <p>
              식단 링크:
              <a :href="menuLink" target="_blank" style="color: blue; text-decoration: underline;">
                {{ menuLink }}
              </a>
            </p>
          </div>

          <div v-else style="position: relative;">
            <v-icon v-if="menuList.some(m => m.posType === 'PREV')" class="arrow-left" @click="goPrev">mdi-chevron-left</v-icon>
            <img :src="menuImage" alt="식단 이미지" class="menu-image" />
            <v-icon v-if="menuList.some(m => m.posType === 'NEXT')" class="arrow-right" @click="goNext">mdi-chevron-right</v-icon>
          </div>
        </div>

        <v-container class="period-container">
          <v-row align="center">
            <v-col cols="12" class="text-center">
              <v-text class="period-text font-weight-bold">{{ period }}</v-text>
              <v-text v-if="isMenuAvailable" class="period-text">최근 업데이트 : {{ updated }}</v-text>
            </v-col>

            <v-btn v-if="editingMenuId" variant="text" color="primary" class="edit-btn" @click="openEditModal">수정</v-btn>
          </v-row>
        </v-container>

        <v-btn class="my-1 func-btns" color="black" dark :density="smAndDown ? 'compact' : 'default'" @click="onNewUploadClick">업로드 하기</v-btn>
        <v-btn class="my-1 func-btns" color="black" dark :density="smAndDown ? 'compact' : 'default'" :to="'/rest/restInfo'">식당정보 조회</v-btn>
        <v-btn class="my-1 func-btns" color="black" dark :density="smAndDown ? 'compact' : 'default'">삭제(관리자)</v-btn>
      </div>
    </div>
  </v-main>

  <MenuForm v-if="showForm" v-model="showForm" :ml-menu-id="editingMenuId" @registered="handleRegistered" />
</template>

<style scoped>
.main-wrapper { padding: 0; margin: 0; }
.full-bleed-background { width: 100%; height: 100%; padding-top: 44px; overflow: hidden; box-sizing: border-box; }
.content-wrapper { display: flex; flex-direction: column; height: 100%; box-sizing: border-box; }
.select-wrapper { display: flex; justify-content: flex-end; height: 10px; flex-shrink: 0; }
.image-wrapper { display: flex; justify-content: center; align-items: center; margin-top: 60px; }
.menu-image { max-width: 100%; height: auto; max-height: 700px; object-fit: contain; }
.period-container { position: relative; }
.period-text { display: block; text-align: center; font-size: 16px; font-weight: 500; color: #333; line-height: 1.6; }
.edit-btn { position: absolute; top: 20px; right: 5px; font-size: 14px; min-width: 50px; padding: 0 8px; transition: background-color 0.2s, color 0.2s; }
.edit-btn:hover { background-color: rgba(33, 150, 243, 0.1); }
.func-btns { font-size: 16px; height: 50px; max-height: 50px; line-height: 50px; white-space: nowrap; }
.arrow-left, .arrow-right { position: absolute; top: 50%; transform: translateY(-50%); z-index: 10; cursor: pointer; font-size: 36px; width: 48px; height: 48px; line-height: 48px; border-radius: 50%; text-align: center; background-color: rgba(0,0,0,0.4); color: white; transition: background-color 0.3s; }
.arrow-left:hover, .arrow-right:hover { background-color: rgba(0,0,0,0.6); }
.arrow-left { left: 10px; }
.arrow-right { right: 10px; }
</style>