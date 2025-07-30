<script setup>
import { ref, watch, onMounted } from 'vue';
import { useDisplay } from 'vuetify';
import axios from 'axios';
import NavMenu2 from '@/components/NavMenu2.vue';
import MenuForm from '@/components/MenuForm.vue';

const { smAndDown } = useDisplay();


// 식당 선택 및 오늘 날짜
const selectedCenter = ref('');
const srchDt = ref(new Date().toISOString().slice(0, 10));

// 식당 리스트
const centerList = ref([]);

// 메뉴 정보
const menuImage = ref('/img/no_mlmenu.png');
const menuLink = ref(''); // 링크용 변수
const period = ref('');
const updated = ref('');
const currentIndex = ref(0);
const menuList = ref([]); 

const currentInfoType = ref('');

// 등록 화면 호출 변수
const showForm = ref(false);

const fetchCenterList = async () => {
  try {
    const response = await axios.get('/api/rest/list', {
      params: { useYn: 'Y' }
    });
    centerList.value = response.data;

    if (centerList.value.length > 0) {
      // 첫번째 식당 선택
      const first = centerList.value[0];
      selectedCenter.value = first.restId;
      currentInfoType.value = first.infoInitType || '';

      // 첫번째 식당의 메뉴 정보 조회
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
      // 단건 처리
      if (menuData) {
        period.value = `${menuData.bgngDt} ~ ${menuData.endDt}`;
        updated.value = menuData.mdfcnDt;
        menuLink.value = menuData.outsdReferUrl || '';
        menuImage.value = '';
      } else {
        // 데이터 없음
        menuImage.value = '';
        menuLink.value = '등록된 식단이 존재하지 않습니다.';
        period.value = '';
        updated.value = '';
      }

    } else {
      // 주간(WEEK) - 다건 처리
      if (Array.isArray(menuData) && menuData.length > 0) {

        // 예: CURRENT 식단 첫 번째로 표시
        const currentIdx = menuData.findIndex(m => m.posType === 'CURRENT');
        currentIndex.value = currentIdx !== -1 ? currentIdx : 0;

        menuList.value = menuData;

        const currentMenu = menuList.value[currentIndex.value];
        period.value = `${currentMenu.bgngDt} ~ ${currentMenu.endDt}`;
        updated.value = currentMenu.mdfcnDt;

        // 이미지 표시
        if (atchList && atchList[0].filePath) {
          menuImage.value = `http://localhost:8080/atch/${atchList[0].filePath.split(/[/\\]/).at(-1)}`;
        } else {
          menuImage.value = '/img/no_mlmenu.png';
        }
        menuLink.value = '';
      } else {
        // 데이터 없음
        menuList.value = [];
        menuImage.value = '/img/no_mlmenu.png';
        menuLink.value = '';
        period.value = '';
        updated.value = '';
      }
    }

  } catch (error) {
    console.error('메뉴 정보 조회 실패:', error);
    menuImage.value = '/img/no_mlmenu.png';
    menuLink.value = '';
    period.value = '';
    updated.value = '';
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
// 초기 데이터 조회
onMounted(async () => {
  await fetchCenterList();
});

// 식당 선택 변경 시 재조회
watch(selectedCenter, async (newVal) => {
  if (newVal) {
    const selected = centerList.value.find(c => c.restId === newVal);
    currentInfoType.value =  selected?.infoInitType || '';
    await fetchMenuInfo(newVal, currentInfoType.value);
  }
});

const handleRegistered = async () => {
  showForm.value = false;
  await fetchMenuInfo(selectedCenter.value, currentInfoType.value); // 재조회
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
          ></v-select>
        </div>

        <!-- 링크 또는 이미지 -->
        <div class="image-wrapper">
          <!-- 링크가 있을 경우 -->
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

          <!-- 이미지가 있을 경우 (화살표 포함) -->
          <div v-else style="position: relative;">
            <!-- 왼쪽 화살표 -->
            <v-icon
              v-if="menuImage && currentIndex > 0"
              class="arrow-left"
              @click="goPrev"
              style="position: absolute; left: 10px; top: 50%; transform: translateY(-50%); font-size: 40px; color: #fff; cursor: pointer;"
            >
              mdi-chevron-left
            </v-icon>

            <!-- 이미지 표시 -->
            <img :src="menuImage" alt="식단 이미지" class="menu-image" />

            <!-- 오른쪽 화살표 -->
            <v-icon
              v-if="menuImage && currentIndex < menuList.length - 1"
              class="arrow-right"
              @click="goNext"
              style="position: absolute; right: 10px; top: 50%; transform: translateY(-50%); font-size: 40px; color: #fff; cursor: pointer;"
            >
              mdi-chevron-right
            </v-icon>
          </div>
        </div>

        <!-- 기간 정보 -->
        <v-container class="text-center">
          <v-text class="period-text font-weight-bold">
            해당기간 : {{ period }}
          </v-text>
          <v-text class="period-text">
            최근 업데이트 : {{ updated }}
          </v-text>
        </v-container>

        <!-- 버튼 목록 -->
        <v-btn block class="my-1 func-btns" color="black" dark :density="smAndDown ? 'compact' : 'default'" @click="showForm = true">업로드 하기</v-btn>
        <v-btn block class="my-1 func-btns" color="black" dark :density="smAndDown ? 'compact' : 'default'" :to="'/rest/restInfo'">식당정보 조회</v-btn>
        <v-btn block class="my-1 func-btns" color="black" dark :density="smAndDown ? 'compact' : 'default'">삭제(관리자)</v-btn>
      </div>
    </div>
  </v-main>
  <MenuForm v-model="showForm" @registered="handleRegistered" />
</template>


<style scoped>
.main-wrapper {
  padding: 0;
  margin: 0;
}

/* 전체 배경 */
.full-bleed-background {
  width: 100%;
  height: 100%;
  padding-top: 44px;
  overflow: hidden;
  box-sizing: border-box;
}

/* 수직 정렬 컨테이너 */
.content-wrapper {
  display: flex;
  flex-direction: column;
  height: 100%;
  box-sizing: border-box;
}

/* select 박스 */
.select-wrapper {
  display: flex;
  justify-content: flex-end;
  height: 10px;
  flex-shrink: 0;
}

/* 이미지 / 링크 */
.image-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 60px;
}

/* 이미지 스타일 */
.menu-image {
  max-width: 100%;
  height: auto;
  object-fit: contain;
}

.period-text {
  display: block;
  text-align: center;
  font-size: 16px;
  font-weight: 500;
  color: #333;
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
  width: 48px;            /* 아이콘 영역 넓이 */
  height: 48px;           /* 아이콘 영역 높이 */
  line-height: 48px;      /* 수직 가운데 정렬 */
  border-radius: 50%;     /* 원형 */
  text-align: center;     /* 아이콘 가운데 정렬 */
  background-color: rgba(0, 0, 0, 0.4); /* 반투명 검정 배경 */
  color: white;           /* 아이콘 색상 */
  transition: background-color 0.3s;
}

.arrow-left:hover,
.arrow-right:hover {
  background-color: rgba(0, 0, 0, 0.6); /* 호버시 좀 더 진하게 */
}
.arrow-left {
  left: 10px;
}
.arrow-right {
  right: 10px;
}
</style>
